package com.varunest.listpullrequests.pullrequests.presenter

import com.varunest.listpullrequests.R
import com.varunest.listpullrequests.pullrequests.interactor.PRInteractor
import com.varunest.listpullrequests.pullrequests.interactor.PRInteractorImpl
import com.varunest.listpullrequests.pullrequests.view.PRViewHelper
import com.varunest.listpullrequests.pullrequests.view.model.ListAdapterItem
import com.varunest.listpullrequests.utils.CommonUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


interface PullRequestPresenter {
    fun start()
    fun onDestroy()
}

class PRPresenterImpl(
    private val viewHelper: PRViewHelper
) :
    PullRequestPresenter {
    private var disposables = CompositeDisposable()
    private val interactor: PRInteractor = PRInteractorImpl()
    private val dataProvider: ListAdapterDataProvider = ListAdapterDataProviderImpl()
    private var prDisposable: Disposable? = null

    override fun start() {
        viewHelper.wireUpWidgets(dataProvider)
        val inputQueryDisposable = viewHelper.queryInputObservable()
            .subscribeOn(Schedulers.io())
            .map { repositoryAddress ->
                val pair = repositoryAddress.split("/")
                if (pair.size == 2) {
                    Pair(pair[0], pair[1])
                } else {
                    Pair("", "")
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { pair -> showPullRequestsForQuery(pair) }

        val scrolledToBottomDisposable = viewHelper.scrolledToBottomObservable()
            .subscribeOn(Schedulers.io())
            .subscribe {
                loadMorePullRequests()
            }

        val prClickDisposable = viewHelper.getPRClickObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { pullRequest -> CommonUtils.openWebPage(pullRequest.url, viewHelper.getContext()) }

        val queryTextWatchDisposable = viewHelper.getQueryTextWatchObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { input ->
                if (input.isEmpty()) {
                    viewHelper.showClearSearchIcon(false)
                    viewHelper.showPRListView(false)
                    interactor.resetState()
                    dataProvider.resetItems()
                    viewHelper.showBigMessage(viewHelper.getContext().getString(R.string.search_a_repo))
                } else {
                    viewHelper.showClearSearchIcon(true)
                }
            }

        val clearSearchDisposable = viewHelper.getClearSearchObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                viewHelper.clearSearchText()
            }

        disposables.add(clearSearchDisposable)
        disposables.add(queryTextWatchDisposable)
        disposables.add(prClickDisposable)
        disposables.add(inputQueryDisposable)
        disposables.add(scrolledToBottomDisposable)
    }

    override fun onDestroy() {
        disposables.clear()
        prDisposable?.dispose()
    }

    private fun loadMorePullRequests() {
        if (!interactor.isLoading()) {
            interactor.loadMorePullRequests()?.apply {
                if (prDisposable != null && prDisposable?.isDisposed != true) {
                    prDisposable?.dispose()
                }
                interactor.setLoading(true)
                dataProvider.addLoader(true)
                prDisposable = subscribeOn(Schedulers.io())
                    .map { pullrequests ->
                        val adapterItems = ArrayList<ListAdapterItem>()
                        for (pr in pullrequests) {
                            adapterItems.add(ListAdapterItem(pr))
                        }
                        adapterItems
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { adapterItems, err ->
                        interactor.setLoading(false)
                        dataProvider.addLoader(false)
                        if (err != null) {
                            var message =
                                viewHelper.getContext().getString(com.varunest.listpullrequests.R.string.unknown_error)
                            if (!CommonUtils.isNetworkAvailable(viewHelper.getContext())) {
                                message = viewHelper.getContext()
                                    .getString(com.varunest.listpullrequests.R.string.network_unavailable)
                            } else if (err.message != null) {
                                message = err.message
                            }
                            viewHelper.showToast(message)
                        } else {
                            if (adapterItems.isEmpty()) {
                                interactor.setMoreAvailable(false)
                            } else {
                                interactor.incrementPage()
                                dataProvider.addItems(adapterItems)
                            }
                        }
                    }
            }
        }
    }

    /**
     * Fetch data and then render the PR on screen.
     *
     *  @param pair contains repo owner in first and repo name in second value.
     */
    private fun showPullRequestsForQuery(pair: Pair<String, String>) {
        viewHelper.showPRListView(false)
        dataProvider.resetItems()
        viewHelper.showBigMessage("")
        interactor.resetState()

        if (!pair.first.isEmpty() && !pair.second.isEmpty()) {
            viewHelper.showFullPageLoader(true)
            interactor.setLoading(true)

            if (prDisposable != null && prDisposable?.isDisposed != true) {
                prDisposable?.dispose()
            }
            prDisposable = interactor.getPullRequests(pair.first, pair.second)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { pullrequests ->
                    val adapterItems = ArrayList<ListAdapterItem>()
                    for (pr in pullrequests) {
                        adapterItems.add(ListAdapterItem(pr))
                    }
                    adapterItems
                }
                .subscribe { adapterItems, err ->
                    interactor.setLoading(false)
                    viewHelper.showFullPageLoader(false)
                    if (err != null) {
                        var message =
                            viewHelper.getContext().getString(com.varunest.listpullrequests.R.string.unknown_error)
                        if (!CommonUtils.isNetworkAvailable(viewHelper.getContext())) {
                            message = viewHelper.getContext().getString(
                                com.varunest.listpullrequests.R.string.network_unavailable
                            )
                        } else {
                            err.message?.let {
                                message = it
                            }
                        }
                        viewHelper.showBigMessage(message)
                    } else {
                        if (adapterItems.isEmpty()) {
                            interactor.setMoreAvailable(false)
                            viewHelper.showPRListView(false)
                            viewHelper.showBigMessage(viewHelper.getContext().getString(com.varunest.listpullrequests.R.string.no_open_pr))
                        } else {
                            viewHelper.showPRListView(true)
                            viewHelper.showBigMessage("")
                            interactor.incrementPage()
                            interactor.setMoreAvailable(true)
                            dataProvider.addItems(adapterItems)
                        }
                    }
                }
        } else {
            viewHelper.showBigMessage(viewHelper.getContext().getString(com.varunest.listpullrequests.R.string.invalid_repo_address))
        }
    }
}