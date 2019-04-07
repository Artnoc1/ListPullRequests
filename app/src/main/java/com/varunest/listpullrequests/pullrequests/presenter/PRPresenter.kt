package com.varunest.listpullrequests.pullrequests.presenter

import com.varunest.listpullrequests.R
import com.varunest.listpullrequests.pullrequests.interactor.PRInteractor
import com.varunest.listpullrequests.pullrequests.interactor.PRInteractorImpl
import com.varunest.listpullrequests.pullrequests.view.PRViewHelper
import com.varunest.listpullrequests.pullrequests.view.model.ListAdapterItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface PullRequestPresenter {
    fun start()
    fun onPause()
    fun onResume()
    fun onDestroy()
}

class PRPresenterImpl(
    private val viewHelper: PRViewHelper
) :
    PullRequestPresenter {
    private var disposables = CompositeDisposable()
    private val interactor: PRInteractor = PRInteractorImpl()
    private val dataProvider: ListAdapterDataProvider = ListAdapterDataProviderImpl()

    override fun start() {
        viewHelper.wireUpWidgets(dataProvider)

        val disposable = viewHelper.queryInputObserver()
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
        disposables.add(disposable)
    }

    override fun onDestroy() {
        disposables.clear()
    }

    override fun onResume() {
        // TODO
    }

    override fun onPause() {
        // TODO
    }

    /**
     * Fetch data and then render the PR on screen.
     *
     *  @param pair contains repo owner in first and repo name in second value.
     */
    private fun showPullRequestsForQuery(pair: Pair<String, String>) {
        if (!pair.first.isEmpty() && !pair.second.isEmpty()) {
            viewHelper.showFullPageLoader(true)
            viewHelper.showPRListView(false)
            viewHelper.showBigMessage("")
            dataProvider.resetItems()

            val disposable = interactor.getPullRequests(pair.first, pair.second)
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
                    viewHelper.showFullPageLoader(false)
                    if (err != null) {
                        err.message?.let {
                            viewHelper.showBigMessage(it)
                        }
                    } else {
                        if (adapterItems.isEmpty()) {
                            viewHelper.showPRListView(false)
                            viewHelper.showBigMessage(viewHelper.getContext().getString(R.string.no_open_pr))
                        } else {
                            viewHelper.showPRListView(true)
                            viewHelper.showBigMessage("")
                            dataProvider.addItems(adapterItems)
                        }

                    }
                }
            disposables.add(disposable)
        } else {
            viewHelper.showToast("Invalid repository address.")
        }
    }
}