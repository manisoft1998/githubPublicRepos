package com.org.githubrepo.model

import com.google.gson.annotations.SerializedName

data class RepoList(
    @SerializedName("id") var id: Long,
    @SerializedName("full_name") var fullName: String,
    @SerializedName("description") var description: String,
    @SerializedName("owner") var owner: Owner,
)

data class Owner(
    @SerializedName("avatar_url") var avatarUrl: String,
)
