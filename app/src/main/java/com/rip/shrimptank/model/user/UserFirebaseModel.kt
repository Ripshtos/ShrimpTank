package com.rip.shrimptank.model.user

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.rip.shrimptank.model.Cloudinary
import com.rip.shrimptank.model.post.Post

class UserFirebaseModel {

    private val db = Firebase.firestore

    companion object {
        const val USERS_COLLECTION_PATH = "users"
    }

    init {
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings { })
        }
        db.firestoreSettings = settings
    }

    fun getAllUsersSince(since: Long, callback: (List<User>) -> Unit) {
        db.collection(USERS_COLLECTION_PATH)
            .whereGreaterThanOrEqualTo("updatedAt", since)
            .get().addOnSuccessListener {
                callback(it.toObjects(User::class.java))
            }.addOnFailureListener {
                Log.d("Error", "Can't get all users: " + it.message)
                callback(listOf())
            }
    }

    fun addUserImage(user: User, selectedImageUri: Uri, callback: () -> Unit) {
        Cloudinary.shared.uploadBitmap(selectedImageUri, onSuccess = { imageUrl ->
            updateUser(user.apply { avatar = imageUrl }) {
                callback()
            }
        }, onError = {
            Log.d("Error", "Can't upload image to cloudinary: $it")
        })
    }

    fun updateUser(user: User?, callback: () -> Unit) {
        db.collection(USERS_COLLECTION_PATH)
            .document(user!!.id).set(user)
            .addOnSuccessListener {
                callback()
            }.addOnFailureListener {
                Log.d("Error", "Can't update this user document: " + it.message)
            }
    }

    fun addUser(user: User, callback: () -> Unit) {
        db.collection(USERS_COLLECTION_PATH).document(user.id).set(user)
            .addOnSuccessListener {
                callback()
            }
    }

    fun getUserById(userId: String, callback: (User?) -> Unit) {
        db.collection(USERS_COLLECTION_PATH).document(userId).get()
            .addOnSuccessListener {
                callback(it.toObject(User::class.java))
            }
    }
}
