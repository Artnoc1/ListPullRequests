package com.varunest.listpullrequests.view

import android.view.View
import kotlinx.android.extensions.LayoutContainer

interface PullRequestViewHelper {

}

class PullRequestViewHelperImpl(val rootView: View) : PullRequestViewHelper, LayoutContainer {
    override val containerView: View = rootView
}