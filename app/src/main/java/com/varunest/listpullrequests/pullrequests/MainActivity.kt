package com.varunest.listpullrequests.pullrequests

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.varunest.listpullrequests.R
import com.varunest.listpullrequests.pullrequests.presenter.PullRequestPresenter
import com.varunest.listpullrequests.pullrequests.presenter.PRPresenterImpl
import com.varunest.listpullrequests.pullrequests.view.PRViewHelperImpl

class MainActivity : AppCompatActivity() {

    lateinit var presenter: PullRequestPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter =
            PRPresenterImpl(
                PRViewHelperImpl(
                    window.decorView.rootView
                )
            )
        presenter.start()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
