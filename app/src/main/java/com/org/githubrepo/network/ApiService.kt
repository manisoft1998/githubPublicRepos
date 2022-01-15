package com.org.githubrepo.network

import com.org.githubrepo.model.RepoList
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("repositories")
     fun getRepositories(): Call<List<RepoList>>
}