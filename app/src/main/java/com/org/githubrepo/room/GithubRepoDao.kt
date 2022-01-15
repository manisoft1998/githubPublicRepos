package com.org.githubrepo.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GithubRepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepo(items: List<GithubRepo>)

    @Query("Select * from GithubRepo")
    fun getRepo(): LiveData<List<GithubRepo>>
}