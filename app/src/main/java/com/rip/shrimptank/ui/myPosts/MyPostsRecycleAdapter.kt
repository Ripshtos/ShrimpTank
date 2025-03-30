package com.rip.shrimptank.ui.myPosts

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.rip.shrimptank.R
import com.rip.shrimptank.model.post.Post
import com.rip.shrimptank.model.post.PostModel
import com.rip.shrimptank.model.user.User

class MyPostsRecycleAdapter(var posts: MutableList<Post>?, var user: User?) :
    RecyclerView.Adapter<PostCardViewHolder>() {

    override fun getItemCount(): Int {
        return posts?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_my_posts_card, parent, false)
        return PostCardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostCardViewHolder, position: Int) {
        val post = posts?.get(position)
        Log.d("TAG", "posts size ${posts?.size}")
        holder.bind(post, user) {
            val action = MyPostsDirections.actionMyPostsToEditPost(post!!)
            Navigation.findNavController(holder.itemView).navigate(action)
        }
    }
}