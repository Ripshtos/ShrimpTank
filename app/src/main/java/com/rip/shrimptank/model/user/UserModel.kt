package com.rip.shrimptank.model.user

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.rip.shrimptank.model.AppDatabase
import com.rip.shrimptank.model.post.PostModel.LoadingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class UserModel private constructor() {

    private val database = AppDatabase.db
    private val firebaseModel = UserFirebaseModel()
    private var executor = Executors.newSingleThreadExecutor()
    private var users: LiveData<MutableList<User>>? = null
    private var lastUpdateTime: LiveData<Long?>? = null
    val usersListLoadingState: MutableLiveData<LoadingState> =
        MutableLiveData(LoadingState.LOADED)


    companion object {
        val instance: UserModel = UserModel()
    }


    fun getAllUsers(): LiveData<MutableList<User>> {
        refreshAllUsers()
        return users!!
    }

    fun refreshAllUsers() {
        if (users == null) {
            users = database.userDao().getAll()
        }

        if (lastUpdateTime == null) {
            lastUpdateTime = database.userDao().getLastUpdateTime()
        }

        usersListLoadingState.postValue(LoadingState.LOADING)

        firebaseModel.getAllUsersSince(lastUpdateTime?.value ?: 0L) { usersFromFirebase ->
            executor.execute {
                usersFromFirebase.forEach { user ->
                    CoroutineScope(Dispatchers.IO).launch {
                        database.userDao().insert(user)
                    }
                }
                usersListLoadingState.postValue(LoadingState.LOADED)
            }
        }
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
            refreshAllUsers()
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