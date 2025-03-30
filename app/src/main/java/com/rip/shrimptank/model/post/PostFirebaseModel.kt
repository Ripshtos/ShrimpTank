package com.rip.shrimptank.model.post

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.rip.shrimptank.model.Cloudinary

class PostFirebaseModel {

    private val db = Firebase.firestore

    companion object {
        const val POSTS_COLLECTION_PATH = "posts"
    }

    init {
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings { })
        }
        db.firestoreSettings = settings
    }

    fun getAllPostsSince(since: Long, callback: (List<Post>) -> Unit) {
        db.collection(POSTS_COLLECTION_PATH)
            .whereGreaterThanOrEqualTo("updatedAt", since)
            .get().addOnSuccessListener {
                callback(it.toObjects(Post::class.java))
            }.addOnFailureListener {
                Log.d("Error", "Can't get all posts: " + it.message)
                callback(listOf())
            }
    }

    fun addPost(post: Post, callback: () -> Unit) {
        db.collection(POSTS_COLLECTION_PATH).document(post.id).set(post)
            .addOnSuccessListener {
                callback()
            }
    }

    fun addPostImage(post: Post, selectedImageUri: Uri, callback: () -> Unit) {
        Cloudinary.shared.uploadBitmap(selectedImageUri, onSuccess = { imageUrl ->
            updatePost(post.apply { postImage = imageUrl }) {
                callback()
            }
        }, onError = {
            Log.d("Error", "Can't upload image to cloudinary: $it")
        })
    }

    fun deletePost(post: Post?, callback: () -> Unit) {
        post!!.isDeleted = true
        post.updatedAt = System.currentTimeMillis()
        db.collection(POSTS_COLLECTION_PATH)
            .document(post.id).set(post).addOnSuccessListener {
                callback()
            }.addOnFailureListener {
                Log.d("Error", "Can't delete this post document: " + it.message)
            }
    }

    fun updatePost(post: Post?, callback: () -> Unit) {
        db.collection(POSTS_COLLECTION_PATH)
            .document(post!!.id).set(post)
            .addOnSuccessListener {
                callback()
            }.addOnFailureListener {
                Log.d("Error", "Can't update this post document: " + it.message)
            }
    }
}
