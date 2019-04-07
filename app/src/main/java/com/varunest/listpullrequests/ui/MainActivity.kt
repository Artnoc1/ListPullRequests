package com.varunest.listpullrequests.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.varunest.listpullrequests.R

class MainActivity : AppCompatActivity() {

    lateinit var presenter: PullRequestPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter =
            PullRequestPresenterImpl(PullRequestViewHelperImpl(window.decorView.rootView))
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
