package com.rip.shrimptank.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.rip.shrimptank.databinding.FragmentCartBinding
import com.rip.shrimptank.interactions.FragmentChangeListener
import com.rip.shrimptank.model.CartEntity
import com.rip.shrimptank.utils.UserInteractions
import com.rip.shrimptank.views.adapters.CartAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private var listener: FragmentChangeListener? = null
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val cartItems = mutableListOf<CartEntity>()
    private lateinit var cartAdapter: CartAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            listener = context
        } else {
            throw RuntimeException("Internal server error")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        setupRecyclerView()
        fetchCartItems()

//        binding.checkoutButton.setOnClickListener {
//            listener?.navigateToFrag(R.id.action_cartFragment_to_checkoutFragment, R.id.cartFragment)
//        }

        return binding.root
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(cartItems)
        binding.recyclerViewCart.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    private fun fetchCartItems() {
        UserInteractions.showLoading(requireActivity())

        firestore.collection("cart")
            .get()
            .addOnSuccessListener { result ->
                cartItems.clear()
                for (document in result) {
                    val item = document.toObject(CartEntity::class.java)
                    cartItems.add(item)
                }
                cartAdapter.notifyDataSetChanged()
                UserInteractions.hideLoad()
            }
            .addOnFailureListener { e ->
                UserInteractions.hideLoad()
                UserInteractions.showDlg(requireActivity(), "Failed to load cart: ${e.message}")
            }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
