package com.varunest.listpullrequests.ui

import com.varunest.listpullrequests.data.network.GithubApiService
import com.varunest.listpullrequests.data.network.RetrofitInstance
import com.varunest.listpullrequests.data.network.model.PullRequest
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

interface PullRequestInteractor {
    fun getPullRequests(repoOwner: String, repoName: String): Single<ArrayList<PullRequest>>
}

class PullRequestInteractorImpl : PullRequestInteractor {
    override fun getPullRequests(repoOwner: String, repoName: String): Single<ArrayList<PullRequest>> {
        val service = RetrofitInstance.instance.create(GithubApiService::class.java)
        return service.getPullRequests(repoOwner, repoName, "open", 1, 10)
    }
}