package com.varunest.listpullrequests.pullrequests.view.model

import com.varunest.listpullrequests.data.network.model.PullRequest

data class ListAdapterItem(var type: Int = TYPE_LOADER) {
    companion object {
        val TYPE_PR = 0x0
        val TYPE_LOADER = 0x1
    }

    var pullRequest: PullRequest? = null

    constructor(pullRequest: PullRequest) : this(TYPE_PR) {
        this.pullRequest = pullRequest
    }
}