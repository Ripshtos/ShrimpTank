package com.rip.shrimptank.room

import androidx.room.RoomDatabase
import com.rip.shrimptank.room.AppDao

//@Database(entities = [], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}