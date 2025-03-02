package com.rip.shrimptank.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rip.shrimptank.model.CartEntity

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartEntity)

    @Update
    suspend fun updateCartItem(item: CartEntity)

    @Delete
    suspend fun deleteCartItem(item: CartEntity)

    @Query("SELECT * FROM cart_table")
    fun getAllCartItems(): LiveData<List<CartEntity>>

    @Query("DELETE FROM cart_table")
    suspend fun clearCart()
}
