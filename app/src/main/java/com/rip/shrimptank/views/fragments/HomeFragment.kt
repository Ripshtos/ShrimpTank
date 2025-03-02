package com.rip.shrimptank.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rip.shrimptank.databinding.FragmentHomeBinding
import com.rip.shrimptank.viewmodel.HomeViewModel
import com.rip.shrimptank.views.adapters.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate via ViewBinding
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Optional title or heading
        binding.titleText.text = "My Shop"

        // Set up the RecyclerView
        val adapter = ProductAdapter(
            products = listOf(),
            onProductClick = { product ->
                // TODO: Navigate to Product Detail, e.g.,
                // findNavController().navigate(R.id.action_homeFragment_to_productFragment, bundle)
            },
            onAddToCartClick = { product ->
                // Add product to local Room cart
                homeViewModel.addToCart(product)
                Toast.makeText(requireContext(), "Added to Cart", Toast.LENGTH_SHORT).show()
            }
        )

        binding.homeRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.homeRecyclerView.adapter = adapter

        // Observe products from Firestore
        homeViewModel.products.observe(viewLifecycleOwner) { productList ->
            adapter.updateProducts(productList)
        }

        // Optionally observe local cart items if you want a cart count
        // homeViewModel.cartItems.observe(viewLifecycleOwner) { cartList ->
        //     // e.g., update a badge or cart icon
        // }
    }
}
