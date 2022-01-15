package com.org.githubrepo.repository

import androidx.lifecycle.LiveData
import com.org.githubrepo.room.GithubRepo
import com.org.githubrepo.room.GithubRepoDao

class DatabaseRepository(private val dao: GithubRepoDao) {
    suspend fun insertRepo(items: List<GithubRepo>) {
        dao.insertRepo(items)
    }

     val getDataFromDB:LiveData<List<GithubRepo>> =dao.getRepo()
}