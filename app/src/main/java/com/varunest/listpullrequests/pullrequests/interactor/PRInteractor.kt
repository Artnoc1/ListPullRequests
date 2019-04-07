package com.varunest.listpullrequests.pullrequests.interactor

import com.varunest.listpullrequests.data.network.GithubApiService
import com.varunest.listpullrequests.data.network.RetrofitInstance
import com.varunest.listpullrequests.data.network.model.PullRequest
import io.reactivex.Single
import java.util.*
import kotlin.collections.ArrayList

interface PRInteractor {
    fun resetState()
    fun setLoading(flag: Boolean)
    fun incrementPage()
    fun getPullRequests(repoOwner: String, repoName: String): Single<ArrayList<PullRequest>>
    fun setMoreAvailable(flag: Boolean)
    fun isLoading(): Boolean
    fun loadMorePullRequests(): Single<ArrayList<PullRequest>>?
}

class PRInteractorImpl : PRInteractor {
    companion object {
        const val PER_PAGE_ITEMS = 20
    }

    private var isLoading = false
    private var isMoreAvailable = true
    private var page = 1
    private var repoOwner: String? = null
    private var repoName: String? = null

    override fun getPullRequests(repoOwner: String, repoName: String): Single<ArrayList<PullRequest>> {
        this.repoName = repoName
        this.repoOwner = repoOwner
        val service = RetrofitInstance.instance.create(GithubApiService::class.java)
        return service.getPullRequests(repoOwner, repoName, "open", page, PER_PAGE_ITEMS)
    }

    override fun loadMorePullRequests(): Single<ArrayList<PullRequest>>? {
        return if (isMoreAvailable && repoName != null && repoOwner != null) {
            val service = RetrofitInstance.instance.create(GithubApiService::class.java)
            service.getPullRequests(repoOwner!!, repoName!!, "open", page, PER_PAGE_ITEMS)
        } else {
            null
        }
    }

    override fun setMoreAvailable(flag: Boolean) {
        isMoreAvailable = flag
    }

    override fun resetState() {
        page = 1
        isLoading = false
    }

    override fun setLoading(flag: Boolean) {
        isLoading = flag
    }

    override fun isLoading(): Boolean {
        return isLoading
    }


    override fun incrementPage() {
        page++
    }

}