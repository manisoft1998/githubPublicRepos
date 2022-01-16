package com.org.githubrepo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.org.githubrepo.R
import com.org.githubrepo.room.GithubRepo
import com.org.githubrepo.util.CommonUtils
import com.org.githubrepo.viewmodel.MyViewModel
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var myViewModel: MyViewModel
    private lateinit var recyclerAdapter: RecyclerAdapter
    private var repoList = ArrayList<GithubRepo>()
    private lateinit var onlineLayout: LinearLayout
    private lateinit var offlineLayout: LinearLayout
    private lateinit var tryAgainBtn: Button
    private var progressBar: ProgressBar? = null
    private lateinit var swipeToRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initRecyclerView()
        initViewModel()

    }

    // init the views
    private fun initViews() {
        setSupportActionBar(findViewById(R.id.tool_bar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        onlineLayout = findViewById(R.id.online_layout)
        offlineLayout = findViewById(R.id.offline_layout)
        tryAgainBtn = findViewById(R.id.try_again_btn)
        progressBar = findViewById(R.id.progressBar1)

        swipeToRefresh = findViewById(R.id.swipe_to_refresh)

        swipeToRefresh.setOnRefreshListener {
            Log.d("MyViewModel", "calling refresh: ")

            Handler(Looper.getMainLooper()).postDelayed({
                swipeToRefresh.isRefreshing = false
            }, 2000)

            myViewModel.getRepoLiveDataListFromServer()

        }

        tryAgainBtn.setOnClickListener {
            if (CommonUtils(this@MainActivity).isNetworkAvailable()) {
                myViewModel.getRepoLiveDataListFromServer()
            } else {
                Toast.makeText(application, "no internet", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    //init the view model
    private fun initViewModel() {

        myViewModel = ViewModelProvider(this@MainActivity)[MyViewModel::class.java]

        showProgress()

        myViewModel.readDataFromDB.observe(this, {
            if (it != null) {
                repoList = it as ArrayList<GithubRepo>
                recyclerAdapter.setData(repoList)
            }
        })
    }

    // show progress state
    private fun showProgress() {

        myViewModel.getIsLoading().observe(this, {
            if (it.equals("load")) {
                if (offlineLayout.isVisible) {
                    offlineLayout.visibility = View.GONE
                    onlineLayout.visibility = View.VISIBLE
                }
                progressBar?.visibility = View.VISIBLE

            } else if (it.equals("completed")) {
                if (offlineLayout.isVisible) {
                    offlineLayout.visibility = View.GONE
                    onlineLayout.visibility = View.VISIBLE
                }
                progressBar?.visibility = View.GONE

            } else {
                onlineLayout.visibility = View.GONE
                offlineLayout.visibility = View.VISIBLE
            }
        })
    }

    //init the recyclerview
    private fun initRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerAdapter = RecyclerAdapter()
        recyclerView.adapter = recyclerAdapter
    }

    // filter the repos from list
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search_view) {
            val searchView = item.actionView as SearchView
            searchView.setOnQueryTextListener(this)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (repoList.size != 0) {
            val filteredRepoList: ArrayList<GithubRepo> = filter(repoList, query!!)
            recyclerAdapter.setFilter(filteredRepoList)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (repoList.size != 0) {
            val filteredRepoList: ArrayList<GithubRepo> = filter(repoList, newText!!)
            recyclerAdapter.setFilter(filteredRepoList)
        }
        return true
    }

    private fun filter(repoList: ArrayList<GithubRepo>, query: String): ArrayList<GithubRepo> {
        val mRepoList = ArrayList<GithubRepo>()
        for (items in repoList) {
            if (items.full_name.toString().lowercase().contains(query)) {
                mRepoList.add(items)
            }
        }
        return mRepoList
    }

}