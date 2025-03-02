package com.rip.shrimptank.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rip.shrimptank.model.Product
import com.google.firebase.firestore.FirebaseFirestore
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

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    // Observing the cart from local DB
    val cartItems = cartRepository.getAllCartItems()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            val productList = productRepository.getProducts()
            _products.value = productList
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addOrIncrementItem(product)
        }
    }
}