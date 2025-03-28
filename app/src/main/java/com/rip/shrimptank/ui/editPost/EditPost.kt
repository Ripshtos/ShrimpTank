package com.rip.shrimptank.ui.editPost

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rip.shrimptank.R
import com.rip.shrimptank.databinding.FragmentEditPostBinding
import com.rip.shrimptank.model.post.PostType
import com.squareup.picasso.Picasso

class EditPost : Fragment() {

    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: EditPostViewModel
    private val args by navArgs<EditPostArgs>()

    private val imageSelectionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                val imageUri: Uri = result.data?.data!!
                val imageSize = getImageSize(imageUri)
                val maxCanvasSize = 5 * 1024 * 1024 // 5MB
                if (imageSize > maxCanvasSize) {
                    Toast.makeText(
                        requireContext(),
                        "Selected image is too large",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.selectedImageURI.postValue(imageUri)
                    viewModel.imageChanged = true
                    binding.postImageView.setImageURI(imageUri)
                }
            } catch (e: Exception) {
                Log.d("EditPost", "Error: $e")
                Toast.makeText(
                    requireContext(), "Error processing result", Toast.LENGTH_SHORT
                ).show()
            }
        }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPostBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this)[EditPostViewModel::class.java]

        initFields()
        defineUpdateButtonClickListener()
        definePickImageClickListener()

        return view
    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private fun definePickImageClickListener() {
        binding.btnPickImage.setOnClickListener {
            defineImageSelectionCallBack()
        }
    }

    private fun defineUpdateButtonClickListener() {
        binding.updateButton.setOnClickListener {
            binding.updateButton.isClickable = false
            viewModel.updatePost {
                findNavController().navigate(R.id.action_editPost_to_myProfile)
                binding.updateButton.isClickable = true
            }
        }
    }

    private fun initFields() {
        val currentPost = args.selectedPost
        viewModel.loadPost(currentPost)

        binding.editPostTitleMultiLine.setText(currentPost.title)
        binding.editPostTextMultiLine.setText(currentPost.text)
        binding.typeAutoCompleteTextView.setText(currentPost.type.toString(), false)

        binding.editPostTitleMultiLine.addTextChangedListener {
            viewModel.title = it.toString().trim()
        }
        binding.editPostTextMultiLine.addTextChangedListener {
            viewModel.description = it.toString().trim()
        }

        binding.typeAutoCompleteTextView.setAdapter(ArrayAdapter(requireContext(), R.layout.list_item, PostType.entries.toTypedArray()))
        binding.typeAutoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            viewModel.type = PostType.valueOf(parent.getItemAtPosition(position).toString())
        }

        viewModel.selectedImageURI.observe(viewLifecycleOwner) { uri ->
            Picasso.get().load(uri).into(binding.postImageView)
        }

        viewModel.titleError.observe(viewLifecycleOwner) {
            if (it.isNotEmpty())
                binding.editPostTitleMultiLine.error = it
        }

        viewModel.descriptionError.observe(viewLifecycleOwner) {
            if (it.isNotEmpty())
                binding.editPostTextMultiLine.error = it
        }
        viewModel.typeError.observe(viewLifecycleOwner) {
            if (it.isNotEmpty())
                binding.typeAutoCompleteTextView.error = it
        }
    }

    @SuppressLint("Recycle")
    private fun getImageSize(uri: Uri?): Long {
        val inputStream = requireContext().contentResolver.openInputStream(uri!!)
        return inputStream?.available()?.toLong() ?: 0
    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private fun defineImageSelectionCallBack() {
        binding.btnPickImage.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
            imageSelectionLauncher.launch(intent)
        }
    }
}