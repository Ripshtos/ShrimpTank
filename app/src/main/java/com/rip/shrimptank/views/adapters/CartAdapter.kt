package com.rip.shrimptank.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rip.shrimptank.databinding.ItemCartBinding
import com.rip.shrimptank.model.CartEntity
import com.squareup.picasso.Picasso

class CartAdapter(private val cartItems: List<CartEntity>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartEntity) {
            binding.textViewName.text = item.name
            binding.textViewPrice.text = "$${item.price}"
            binding.textViewQuantity.text = "Qty: ${item.quantity}"

            Picasso.get()
                .load(item.imageUrl)
                .placeholder(com.rip.shrimptank.R.drawable.placeholder) // Optional: Placeholder image
                .error(com.rip.shrimptank.R.drawable.error_image) // Optional: Error image
                .into(binding.imageViewItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount() = cartItems.size
}
