package com.org.githubrepo.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.org.githubrepo.R
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
    private lateinit var recyclerAdapter: RecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        makeApiCall()
        initViewModel()
    }

    private fun initViewModel() {
        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        myViewModel.readDataFromDB.observe(this, {
            if (it != null) {
                /* if (it.size == 0) {
                     if (isNetworkAvailable(this)) {
                         makeApiCall()
                     } else {

                     }
                 }*/
                recyclerAdapter.setData(it as ArrayList<GithubRepo>)
//                Toast.makeText(this@MainActivity, it.size.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerAdapter = RecyclerAdapter()
        recyclerView.adapter = recyclerAdapter
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
                        Toast.makeText(this@MainActivity, "api called", Toast.LENGTH_SHORT).show()
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


    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}