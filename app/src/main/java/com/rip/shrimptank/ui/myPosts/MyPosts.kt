package com.rip.shrimptank.ui.myPosts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rip.shrimptank.databinding.FragmentExploreBinding
import com.rip.shrimptank.model.post.PostModel

class MyPosts : Fragment() {
    private var postsRecyclerView: RecyclerView? = null
    private var adapter: MyPostsRecycleAdapter? = null
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MyPostsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this)[MyPostsViewModel::class.java]

        postsRecyclerView = binding.postsExplore
        postsRecyclerView?.setHasFixedSize(true)
        postsRecyclerView?.layoutManager = LinearLayoutManager(context)
        adapter = MyPostsRecycleAdapter(viewModel.posts.value, viewModel.user.value)

        postsRecyclerView?.adapter = adapter

        viewModel.posts.observe(viewLifecycleOwner) {
            adapter?.posts = it
            adapter?.notifyDataSetChanged()
        }

        viewModel.user.observe(viewLifecycleOwner) {
            adapter?.user = it
            adapter?.notifyDataSetChanged()
        }

        binding.pullToRefresh.setOnRefreshListener {
            viewModel.reloadData()
        }

        viewModel.postsListLoadingState.observe(viewLifecycleOwner) { state ->
            binding.pullToRefresh.isRefreshing = state == PostModel.LoadingState.LOADING
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.reloadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}