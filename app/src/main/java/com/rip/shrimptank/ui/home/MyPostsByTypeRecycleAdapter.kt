package com.rip.shrimptank.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rip.shrimptank.R
import com.rip.shrimptank.model.post.Post

class MyPostsByTypeRecycleAdapter(var posts: MutableList<Post>?) :
    RecyclerView.Adapter<HomePostCardViewHolder>() {

    override fun getItemCount(): Int {
        return posts?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePostCardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_home_post_card, parent, false)
        return HomePostCardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomePostCardViewHolder, position: Int) {
        val post = posts?.get(position)
        Log.d("TAG", "posts size ${posts?.size}")
        holder.bind(post)
    }
}