package com.rip.shrimptank.model

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rip.shrimptank.ShrimpTank
import com.rip.shrimptank.model.post.Post
import com.rip.shrimptank.model.post.PostDAO
import com.rip.shrimptank.model.user.User
import com.rip.shrimptank.model.user.UserDAO


@Database(entities = [User::class, Post::class], version = 8, exportSchema = true)
abstract class AppDatabaseRepository : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun postDao(): PostDAO
}

object AppDatabase {
    val db: AppDatabaseRepository by lazy {
        val context = ShrimpTank.Globals.context
            ?: throw IllegalStateException("Application context not available")

        Room.databaseBuilder(
            context,
            AppDatabaseRepository::class.java,
            "my_store_database"
        ).fallbackToDestructiveMigration()
            .build()
    }
}