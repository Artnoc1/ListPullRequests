package com.varunest.listpullrequests.presenter

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import com.varunest.listpullrequests.view.PullRequestViewHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

interface PullRequestPresenter {
    fun start()
    fun onPause()
    fun onResume()
    fun onDestroy()
}

class PullRequestPresenterImpl(private val viewHelper: PullRequestViewHelper) : PullRequestPresenter {
    private var disposables = CompositeDisposable()

    override fun start() {
        val disposable = viewHelper.queryInputObserver()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { query ->
                viewHelper.showToast(query)
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