package com.varunest.listpullrequests.data.network.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login") val name: String,
    @SerializedName("avatar_url") val avatarUrl: String
)