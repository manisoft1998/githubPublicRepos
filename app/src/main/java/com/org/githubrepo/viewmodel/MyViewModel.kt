package com.org.githubrepo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.org.githubrepo.model.RepoList
import com.org.githubrepo.network.ApiService
import com.org.githubrepo.network.RetroInstance
import com.org.githubrepo.repository.DatabaseRepository
import com.org.githubrepo.room.AppDatabase
import com.org.githubrepo.room.GithubRepo
import com.org.githubrepo.util.CommonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyViewModel(application: Application) : AndroidViewModel(application) {

    private var databaseRepository: DatabaseRepository
    val readDataFromDB: LiveData<List<GithubRepo>>
    var isLoading = MutableLiveData("completed")
    private val context = application


    init {
        val dao = AppDatabase.getDatabase(application).githubRepoDao()
        databaseRepository = DatabaseRepository(dao)

        readDataFromDB = databaseRepository.getDataFromDB
        if (CommonUtils(application).isNetworkAvailable()) {
            getRepoLiveDataListFromServer()
        }
    }


    fun addData(items: List<GithubRepo>) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.insertRepo(items)
        }
    }

    // get data from server
    fun getRepoLiveDataListFromServer() {
        if (CommonUtils(context).isNetworkAvailable()) {
            isLoading.postValue("load")
            val retrofitInstance =
                RetroInstance.getRetrofitInstance().create(ApiService::class.java)
            retrofitInstance.getRepositories().enqueue(object : Callback<List<RepoList>> {
                override fun onResponse(
                    call: Call<List<RepoList>>,
                    response: Response<List<RepoList>>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val myList = ArrayList<GithubRepo>()

                            for (items in response.body()!!) {
                                myList.add(
                                    GithubRepo(
                                        items.id,
                                        items.fullName,
                                        items.description,
                                        items.owner.avatarUrl
                                    )
                                )
                            }

                            addData(myList)
                            isLoading.postValue("completed")

                        } else {
                            isLoading.postValue("completed")
                        }
                    } else {
                        isLoading.postValue("completed")
                    }
                }

                override fun onFailure(call: Call<List<RepoList>>, t: Throwable) {
                    isLoading.postValue("completed")
                }
            })
        } else {
            isLoading.postValue("error")
        }

    }

    fun getIsLoading(): LiveData<String?> {
        return isLoading
    }

}
