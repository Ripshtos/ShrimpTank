package com.rip.shrimptank.model

data class CartItem(
    val name: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val quantity: Int = 1
)
