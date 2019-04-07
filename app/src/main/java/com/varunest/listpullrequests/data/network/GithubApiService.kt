package com.varunest.listpullrequests.data.network

import com.varunest.listpullrequests.data.network.model.PullRequest
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    @GET("/repos/{repoOwner}/{repoName}/pulls")
    fun getPullRequests(
        @Path("repoOwner") repoOwner: String,
        @Path("repoName") repoName: String,
        @Query("state") state: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): Single<ArrayList<PullRequest>>
}