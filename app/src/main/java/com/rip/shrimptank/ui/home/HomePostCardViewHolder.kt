package com.rip.shrimptank.ui.home

import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rip.shrimptank.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.rip.shrimptank.model.post.Post
import com.rip.shrimptank.model.post.PostModel
import com.rip.shrimptank.model.user.User
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date

class HomePostCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val postImageView: ImageView?
    val title: TextView?
    val postDescription: TextView?
    val date: TextView?


    init {
        postImageView = itemView.findViewById(R.id.CardImage)
        title = itemView.findViewById(R.id.PostTitle)
        postDescription = itemView.findViewById(R.id.PostDescription)
        date = itemView.findViewById(R.id.date)
    }

    @SuppressLint("SetTextI18n")
    fun bind(post: Post?) {
        Picasso.get()
            .load(post?.postImage)
            .into(postImageView)
        title?.text = post?.title
        postDescription?.text = post?.text
        date?.text = formatDate(post?.createdAt!!)
    }

    fun formatDate(date: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy, HH:mm")
        return sdf.format(Date(date))
    }
}