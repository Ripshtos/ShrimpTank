package com.rip.shrimptank.repository

import com.rip.shrimptank.model.CartEntity
import com.rip.shrimptank.model.Product
import com.rip.shrimptank.room.CartDao
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {
    fun getAllCartItems() = cartDao.getAllCartItems()

    suspend fun addOrIncrementItem(product: Product) {
        val currentItems = cartDao.getAllCartItems().value ?: emptyList()

        val existing = currentItems.find { it.productId == product.id }
        if (existing != null) {
            val updated = existing.copy(quantity = existing.quantity + 1)
            cartDao.updateCartItem(updated)
        } else {
            val newItem = CartEntity(
                productId = product.id,
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
                quantity = 1
            )
            cartDao.insertCartItem(newItem)
        }
    }

    suspend fun removeItem(item: CartEntity) {
        cartDao.deleteCartItem(item)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }
}
