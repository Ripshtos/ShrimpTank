package com.rip.shrimptank.model.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDAO {
    @Query("SELECT * FROM user order by createdAt desc")
    fun getAll(): LiveData<MutableList<User>>

    @Query("SELECT * FROM user where id = :userId")
    fun getUserById(userId: String): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("SELECT MAX(updatedAt) FROM user")
    fun getLastUpdateTime(): LiveData<Long?>
}