package com.rip.shrimptank.ui.newPost

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.rip.shrimptank.model.post.Post
import com.rip.shrimptank.model.post.PostModel
import com.rip.shrimptank.model.post.PostType
import java.util.UUID

class NewPostViewModel : ViewModel() {
    var selectedImageURI: MutableLiveData<Uri> = MutableLiveData()
    var title: String = ""
    var description: String = ""
    var type: PostType? = null
    var titleError = MutableLiveData("")
    var descriptionError = MutableLiveData("")
    var typeError = MutableLiveData("")
    var imageError = MutableLiveData("")
    private val auth = Firebase.auth

    val isUploading = MutableLiveData<Boolean>()

    fun createPost(callback: () -> Unit) {
        if (validatePostUpdate()) {
            isUploading.value = true
            val userId = auth.currentUser!!.uid

            val post = Post(
                UUID.randomUUID().toString(),
                type!!,
                userId,
                title,
                description
            )

            PostModel.instance.addPost(post, selectedImageURI.value!!) {
                isUploading.value = false
                callback()
            }
        } else {
            isUploading.value = false
        }
    }

    private fun validatePostUpdate(
    ): Boolean {
        var valid = true

        if (title.isEmpty()) {
            titleError.postValue("Title cannot be empty")
            valid = false
        }
        if (description.isEmpty()) {
            descriptionError.postValue("Description cannot be empty")
            valid = false
        }
        if (type == null) {
            typeError.postValue("Type cannot be empty")
            valid = false
        }

        if (selectedImageURI.value == null) {
            imageError.postValue("Please select an image")
            valid = false
        }

        return valid
    }
}