package com.varunest.listpullrequests.data.network.model

import com.google.gson.annotations.SerializedName

data class PullRequest(
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("url") val url: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("user") val user: User
)