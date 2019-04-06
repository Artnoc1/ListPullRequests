package com.varunest.listpullrequests

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.varunest.listpullrequests.presenter.PullRequestPresenter
import com.varunest.listpullrequests.presenter.PullRequestPresenterImpl
import com.varunest.listpullrequests.view.PullRequestViewHelper
import com.varunest.listpullrequests.view.PullRequestViewHelperImpl

class MainActivity : AppCompatActivity() {

    lateinit var presenter: PullRequestPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = PullRequestPresenterImpl(PullRequestViewHelperImpl(window.decorView.rootView))
        presenter.start()
    }

    override fun onPause() {
        presenter.onPause()
        super.onPause()
    }

    override fun onResume() {
        presenter.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
