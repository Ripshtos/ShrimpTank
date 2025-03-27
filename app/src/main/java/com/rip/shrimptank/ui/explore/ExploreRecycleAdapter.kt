package com.rip.shrimptank.ui.explore

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rip.shrimptank.R
import com.rip.shrimptank.model.post.Post
import com.rip.shrimptank.model.user.User

class ExploreRecycleAdapter(var posts: MutableList<Post>?, var users: MutableList<User>?) :
    RecyclerView.Adapter<ExploreViewHolder>() {

    override fun getItemCount(): Int {
        return posts?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.posts_explore_card, parent, false)
        return ExploreViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val post = posts?.get(position)
        holder.bind(post, users?.find { it.id == post?.userId })
    }
}