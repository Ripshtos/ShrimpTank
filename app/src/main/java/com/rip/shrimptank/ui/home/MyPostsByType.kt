package com.rip.shrimptank.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rip.shrimptank.databinding.FragmentHomePostScrollBinding
import com.rip.shrimptank.model.post.PostType

open class MyPostsByType(private val type: PostType) : Fragment() {
    private var postsRecyclerView: RecyclerView? = null
    private var adapter: MyPostsByTypeRecycleAdapter? = null
    private var _binding: FragmentHomePostScrollBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MyPostsByTypeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePostScrollBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this)[MyPostsByTypeViewModel::class.java]
        viewModel.setType(type)
        postsRecyclerView = binding.postList
        postsRecyclerView?.setHasFixedSize(true)
        postsRecyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = MyPostsByTypeRecycleAdapter(viewModel.posts?.value)

        postsRecyclerView?.adapter = adapter

        viewModel.posts?.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.postList.visibility = View.GONE
                binding.noPosts.visibility = View.VISIBLE
            } else {
                binding.postList.visibility = View.VISIBLE
                binding.noPosts.visibility = View.GONE
            }
            adapter?.posts = it
            adapter?.notifyDataSetChanged()
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