package com.org.githubrepo.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GithubRepo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun githubRepoDao(): GithubRepoDao

    companion object {
        var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            val instance = INSTANCE
            if (instance != null)
                return instance

            synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "GithubRepoDB"
                ).build()

                INSTANCE = instance
                return instance

            }

        }
    }
}