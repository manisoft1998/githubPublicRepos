package com.org.githubrepo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.org.githubrepo.model.RepoList
import com.org.githubrepo.repository.DatabaseRepository
import com.org.githubrepo.room.AppDatabase
import com.org.githubrepo.room.GithubRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel(application: Application) : AndroidViewModel(application) {
    private var databaseRepository: DatabaseRepository
    val readDataFromDB: LiveData<List<GithubRepo>>

    init {
        val dao = AppDatabase.getDatabase(application).githubRepoDao()
        databaseRepository = DatabaseRepository(dao)
        readDataFromDB = databaseRepository.getDataFromDB
    }

    fun addData(items: List<GithubRepo>) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.insertRepo(items)
        }
    }
}
