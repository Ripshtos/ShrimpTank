package com.rip.shrimptank.ui.explore

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rip.shrimptank.R
import com.rip.shrimptank.model.post.Post
import com.rip.shrimptank.model.user.User
import com.squareup.picasso.Picasso

class ExploreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val postImageView: ImageView?
    val profileImageView: ImageView?
    val profileName: TextView?
    val postTitle: TextView?
    val postDescription: TextView?
    val type: TextView?


    init {
        postImageView = itemView.findViewById(R.id.CardImage)
        profileImageView = itemView.findViewById(R.id.ProfileImageView)
        profileName = itemView.findViewById(R.id.ProfileName)
        postTitle = itemView.findViewById(R.id.postTitle)
        postDescription = itemView.findViewById(R.id.postDescription)
        type = itemView.findViewById(R.id.postType)
    }

    fun bind(post: Post?, user: User?) {
        Picasso.get()
            .load(post?.postImage)
            .into(postImageView)
        Picasso.get()
            .load(user?.avatar)
            .into(profileImageView)
        profileName?.text = user?.name ?: ""
        postTitle?.text = post?.title
        postDescription?.text = post?.text
        type?.text = "Type: ${post?.type}"
    }
}