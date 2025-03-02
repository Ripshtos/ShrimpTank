package com.rip.shrimptank.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.rip.shrimptank.model.Product
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getProducts(): List<Product> {
        return try {
            val snapshot = firestore.collection("products").get().await()
            snapshot.toObjects(Product::class.java)
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error fetching products", e)
            emptyList()
        }
    }
}
