package com.rip.shrimptank.model.user

import android.net.Uri
import androidx.lifecycle.LiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.rip.shrimptank.model.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserModel private constructor() {

    private val database = AppDatabase.db
    private val firebaseModel = UserFirebaseModel()
    private val users: LiveData<MutableList<User>>? = null


    companion object {
        val instance: UserModel = UserModel()
    }

    fun getCurrentUser(): LiveData<User> {
        return database.userDao().getUserById(Firebase.auth.currentUser?.uid!!)
    }

    fun updateCurrentUser(callback: () -> Unit) {
        firebaseModel.getUserById(Firebase.auth.currentUser?.uid!!) {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    database.userDao().insert(it)
                }
                callback()
            }
        }
    }

    fun updateUser(user: User?, callback: () -> Unit) {
        firebaseModel.updateUser(user) {
            updateCurrentUser {}
            callback()
        }
    }

    fun updateUserImage(user: User, selectedImageUri: Uri, callback: () -> Unit) {
        firebaseModel.addUserImage(user, selectedImageUri, callback)
    }

    fun addUser(user: User, selectedImageUri: Uri, callback: () -> Unit) {
        firebaseModel.addUser(user) {
            firebaseModel.addUserImage(user, selectedImageUri) {
                updateCurrentUser(callback)
            }
        }
    }
}