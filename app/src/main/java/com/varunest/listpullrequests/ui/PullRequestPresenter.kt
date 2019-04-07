package com.varunest.listpullrequests.ui

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface PullRequestPresenter {
    fun start()
    fun onPause()
    fun onResume()
    fun onDestroy()
}

class PullRequestPresenterImpl(
    private val viewHelper: PullRequestViewHelper
) :
    PullRequestPresenter {
    private var disposables = CompositeDisposable()
    private val interactor = PullRequestInteractorImpl()

    override fun start() {
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
            .subscribe { pair ->
                interactor.getPullRequests(pair.first, pair.second)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { pullrequests, err ->
                        if (err != null) {
                            viewHelper.showToast(err.message)
                        } else {
                            // TODO: show pull requests
                        }
                    }
            }
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

}