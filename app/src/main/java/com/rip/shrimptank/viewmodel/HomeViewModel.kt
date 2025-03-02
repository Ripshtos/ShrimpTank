package com.rip.shrimptank.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rip.shrimptank.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.rip.shrimptank.repository.CartRepository
import com.rip.shrimptank.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        firestore.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val productList = result.toObjects<Product>()
                _products.value = productList
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addOrIncrementItem(product)
        }
    }
}