package com.rip.shrimptank.ui.editPost

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rip.shrimptank.model.post.Post
import com.rip.shrimptank.model.post.PostModel
import com.rip.shrimptank.model.post.PostType

class EditPostViewModel : ViewModel() {
    var imageChanged = false
    var selectedImageURI: MutableLiveData<Uri> = MutableLiveData()
    var post: Post? = null

    var title: String? = null
    var description: String? = null
    var type: PostType? = null
    var titleError = MutableLiveData("")
    var descriptionError = MutableLiveData("")
    var typeError = MutableLiveData("")

    fun loadPost(post: Post) {
        this.post = post
        this.title = post.title
        this.description = post.text
        this.type = post.type

        selectedImageURI.postValue(Uri.parse(post.postImage))
    }

    fun updatePost(callback: () -> Unit) {
        if (validatePostUpdate()) {
            val updatedPost = Post(
                post!!.id,
                type!!,
                post!!.userId,
                title!!,
                description!!,
                postImage = post!!.postImage,
                createdAt = post!!.createdAt
            )

            PostModel.instance.updatePost(updatedPost) {
                if (imageChanged) {
                    PostModel.instance.updatePostImage(post!!, selectedImageURI.value!!) {
                        callback()
                    }
                } else {
                    callback()
                }
            }
        }
    }

    private fun validatePostUpdate(
    ): Boolean {
        if (title != null && title!!.isEmpty()) {
            titleError.postValue("Title cannot be empty")
            return false
        }
        if (description != null && description!!.isEmpty()) {
            descriptionError.postValue("Description cannot be empty")
            return false
        }
        if (type == null) {
            typeError.postValue("Type cannot be empty")
            return false
        }
        return true
    }
}