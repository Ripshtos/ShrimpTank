package com.rip.shrimptank.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    val cartId: Int = 0,
    val productId: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val quantity: Int
)
