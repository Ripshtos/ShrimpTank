package com.rip.shrimptank.ui.myPosts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rip.shrimptank.model.post.Post
import com.rip.shrimptank.model.post.PostModel
import com.rip.shrimptank.model.user.User
import com.rip.shrimptank.model.user.UserModel

class MyPostsViewModel : ViewModel() {
    val posts: LiveData<MutableList<Post>> = PostModel.instance.getMyPosts()
    val user: LiveData<User> = UserModel.instance.getCurrentUser()
    val postsListLoadingState: MutableLiveData<PostModel.LoadingState> =
        PostModel.instance.postsListLoadingState

    fun reloadData() {
        UserModel.instance.refreshAllUsers()
        PostModel.instance.refreshAllPosts()
    }
}