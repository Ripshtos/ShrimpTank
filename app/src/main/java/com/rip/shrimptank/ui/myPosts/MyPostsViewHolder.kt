package com.rip.shrimptank.ui.myPosts

import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rip.shrimptank.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rip.shrimptank.model.post.Post
import com.rip.shrimptank.model.user.User
import com.squareup.picasso.Picasso

class MyPostsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val postImageView: ImageView?
    val profileImageView: ImageView?
    val profileName: TextView?
    val title: TextView?
    val postDescription: TextView?
    val type: TextView?
    val editButton: Button
    val deleteButton: Button


    init {
        postImageView = itemView.findViewById(R.id.CardImage)
        profileImageView = itemView.findViewById(R.id.ProfileImageView)
        profileName = itemView.findViewById(R.id.ProfileName)
        title = itemView.findViewById(R.id.PostTitle)
        postDescription = itemView.findViewById(R.id.PostDescription)
        type = itemView.findViewById(R.id.PostType)
        editButton = itemView.findViewById(R.id.EditButton)
        deleteButton = itemView.findViewById(R.id.DeleteButton)
    }

    @SuppressLint("SetTextI18n")
    fun bind(
        post: Post?,
        user: User?,
        editPostClickListener: () -> Unit,
        deletePostClickListener: () -> Unit
    ) {
        Picasso.get()
            .load(post?.postImage)
            .into(postImageView)
        Picasso.get()
            .load(user?.avatar)
            .into(profileImageView)
        profileName?.text = user?.name ?: ""
        title?.text = post?.title
        postDescription?.text = post?.text
        type?.text = "Type: ${post?.type}"
        deleteButton.setOnClickListener {
            MaterialAlertDialogBuilder(itemView.context)
                .setTitle("Delete Post")
                .setMessage("Do you want to delete this post?")
                .setNeutralButton("Cancel") { _, _ ->
                }
                .setPositiveButton("Delete") { _, _ ->
                    deletePostClickListener()
                }
                .show()
        }
        editButton.setOnClickListener {
            editPostClickListener()
        }
    }
}