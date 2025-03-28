package com.rip.shrimptank.model.post

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.rip.shrimptank.model.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class PostModel private constructor() {

    enum class LoadingState {
        LOADING,
        LOADED
    }

    private val database = AppDatabase.db
    private var executor = Executors.newSingleThreadExecutor()
    private val firebaseModel = PostFirebaseModel()
    private var posts: LiveData<MutableList<Post>>? = null
    private var lastUpdateTime: LiveData<Long?>? = null
    val postsListLoadingState: MutableLiveData<LoadingState> =
        MutableLiveData(LoadingState.LOADED)


    companion object {
        val instance: PostModel = PostModel()
    }

    fun getAllPosts(): LiveData<MutableList<Post>> {
        refreshAllPosts()
        return posts!!
    }

    fun getMyPosts(): LiveData<MutableList<Post>> {
        refreshAllPosts()
        return database.postDao().getPostsByUserId(Firebase.auth.currentUser?.uid!!)
    }

    fun refreshAllPosts() {
        if (posts == null) {
            posts = database.postDao().getAll()
        }

        if (lastUpdateTime == null) {
            lastUpdateTime = database.postDao().getLastUpdateTime()
        }

        postsListLoadingState.postValue(LoadingState.LOADING)

        firebaseModel.getAllPostsSince(lastUpdateTime?.value ?: 0L) { postsFromFirebase ->
            executor.execute {
                postsFromFirebase.forEach { post ->
                    CoroutineScope(Dispatchers.IO).launch {
                        if (post.isDeleted) {
                            database.postDao().delete(post)
                        } else {
                            database.postDao().insert(post)
                        }
                    }
                }
                postsListLoadingState.postValue(LoadingState.LOADED)
            }
        }
    }

    fun getUserPosts(): LiveData<MutableList<Post>> {
        refreshAllPosts()
        return database.postDao().getPostsByUserId(Firebase.auth.currentUser?.uid!!)
    }


    fun addPost(post: Post, selectedImageUri: Uri, callback: () -> Unit) {
        firebaseModel.addPost(post) {
            firebaseModel.addPostImage(post, selectedImageUri) {
                refreshAllPosts()
                callback()
            }
        }
    }

    fun deletePost(post: Post?, callback: () -> Unit) {
        firebaseModel.deletePost(post) {
            refreshAllPosts()
            callback()
        }
    }

    fun updatePost(post: Post?, callback: () -> Unit) {
        firebaseModel.updatePost(post) {
            refreshAllPosts()
            callback()
        }
    }

    fun updatePostImage(post: Post, selectedImageUri: Uri, callback: () -> Unit) {
        firebaseModel.addPostImage(post, selectedImageUri) {
            callback()
        }
    }
}