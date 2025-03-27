package com.rip.shrimptank.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rip.shrimptank.model.post.Post
import com.rip.shrimptank.model.post.PostModel
import com.rip.shrimptank.model.user.User
import com.rip.shrimptank.model.user.UserModel

class ExploreViewModel : ViewModel() {
    val posts: LiveData<MutableList<Post>> = PostModel.instance.getAllPosts()
    val users: LiveData<MutableList<User>> = UserModel.instance.getAllUsers()
    val postsListLoadingState: MutableLiveData<PostModel.LoadingState> =
        PostModel.instance.postsListLoadingState

    fun reloadData() {
        UserModel.instance.refreshAllUsers()
        PostModel.instance.refreshAllPosts()
    }
}