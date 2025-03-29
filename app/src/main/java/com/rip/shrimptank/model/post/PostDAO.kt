package com.rip.shrimptank.model.post

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostDAO {
    @Query("SELECT * FROM post order by createdAt desc")
    fun getAll(): LiveData<MutableList<Post>>

    @Query("SELECT * FROM post WHERE userId = :userId order by createdAt desc")
    fun getPostsByUserId(userId: String): LiveData<MutableList<Post>>

    @Query("SELECT * FROM post WHERE userId = :userId and type = :type order by createdAt desc")
    fun getPostsByUserIdAndType(userId: String, type: PostType): LiveData<MutableList<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post)

    @Delete
    fun delete(post: Post)

    @Query("SELECT MAX(updatedAt) FROM post")
    fun getLastUpdateTime(): LiveData<Long?>

    @Query("SELECT MAX(updatedAt) FROM post")
    fun getLastUpdateTimeDirectly(): Long?
}