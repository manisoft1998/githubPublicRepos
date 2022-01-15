package com.org.githubrepo.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GithubRepo(
    @PrimaryKey
    val id: Long,
    val full_name: String?,
    val description: String?,
    val avatar_url: String?
)
