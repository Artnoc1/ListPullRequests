package com.varunest.listpullrequests.presenter

import com.varunest.listpullrequests.view.PullRequestViewHelper

interface PullRequestPresenter {
    fun setViewHelper(viewHelper: PullRequestViewHelper)
    fun onPause()
    fun onResume()
    fun onDestroy()
}

class PullRequestPresenterImpl : PullRequestPresenter {
    private var viewHelper : PullRequestViewHelper? = null

    override fun setViewHelper(viewHelper: PullRequestViewHelper) {
        this.viewHelper = viewHelper
    }

    override fun onDestroy() {
        TODO("not implemented")
    }

    override fun onResume() {
        TODO("not implemented")
    }

    override fun onPause() {
        TODO("not implemented")
    }
}