package com.org.githubrepo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.org.githubrepo.model.RepoList
import com.org.githubrepo.network.ApiService
import com.org.githubrepo.network.RetroInstance
import com.org.githubrepo.room.GithubRepo
import com.org.githubrepo.viewmodel.MyViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"
    private lateinit var myViewModel: MyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        makeApiCall()

        myViewModel.readDataFromDB.observe(this, {
            if (it != null) {
                Toast.makeText(this@MainActivity, it.size.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun makeApiCall() {
        val call = RetroInstance.getRetrofitInstance().create(ApiService::class.java)
        call.getRepositories().enqueue(object : Callback<List<RepoList>> {
            override fun onResponse(
                call: Call<List<RepoList>>,
                response: Response<List<RepoList>>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        Log.i(TAG, "success: ")
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
                        myViewModel.addData(myList)
                    }
                }
            }

            override fun onFailure(call: Call<List<RepoList>>, t: Throwable) {
                Log.e(TAG, "onFailure: " + t.message)
            }

        })
    }
}