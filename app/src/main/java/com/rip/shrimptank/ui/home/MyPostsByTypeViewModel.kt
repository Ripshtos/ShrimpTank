package com.rip.shrimptank.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rip.shrimptank.model.post.Post
import com.rip.shrimptank.model.post.PostModel
import com.rip.shrimptank.model.post.PostType

class MyPostsByTypeViewModel : ViewModel() {
    var posts: LiveData<MutableList<Post>>? = null
    val postsListLoadingState: MutableLiveData<PostModel.LoadingState> =
        PostModel.instance.postsListLoadingState

    fun reloadData() {
        PostModel.instance.refreshAllPosts()
    }

    fun setType(type: PostType) {
        posts = PostModel.instance.getMyPostsByType(type)
    }
}