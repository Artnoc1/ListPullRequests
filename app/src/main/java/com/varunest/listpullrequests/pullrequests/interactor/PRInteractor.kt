package com.varunest.listpullrequests.pullrequests.interactor

import com.varunest.listpullrequests.data.network.GithubApiService
import com.varunest.listpullrequests.data.network.RetrofitInstance
import com.varunest.listpullrequests.data.network.model.PullRequest
import io.reactivex.Single
import java.util.*

interface PRInteractor {
    fun getPullRequests(repoOwner: String, repoName: String): Single<ArrayList<PullRequest>>
}

class PRInteractorImpl : PRInteractor {
    override fun getPullRequests(repoOwner: String, repoName: String): Single<ArrayList<PullRequest>> {
        val service = RetrofitInstance.instance.create(GithubApiService::class.java)
        return service.getPullRequests(repoOwner, repoName, "open", 1, 100)
    }
}