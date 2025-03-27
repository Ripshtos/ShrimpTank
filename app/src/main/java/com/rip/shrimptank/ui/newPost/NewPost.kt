package com.rip.shrimptank.ui.newPost

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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rip.shrimptank.R
import com.rip.shrimptank.databinding.FragmentNewPostBinding

class NewPost : Fragment() {
    private var _binding: FragmentNewPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NewPostViewModel


    private fun showPickImageLoading(isLoading: Boolean) {
        binding.btnPickImage.isEnabled = !isLoading
        binding.pickImageProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showUploadLoading(isLoading: Boolean) {
        binding.uploadButton.isEnabled = !isLoading
        binding.uploadProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private val imageSelectionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            showPickImageLoading(false)
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
                    binding.tvShowImageView.setImageURI(imageUri)
                }
            } catch (e: Exception) {
                Log.d("NewPost", "Error: $e")
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
        _binding = FragmentNewPostBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this)[NewPostViewModel::class.java]

        initFields()
        defineUploadButtonClickListener()
        definePickImageClickListener()

        viewModel.isUploading.observe(viewLifecycleOwner) { isUploading ->
            showUploadLoading(isUploading)
        }

        return view
    }

    private fun initFields() {
        binding.editTextTextMultiLine.addTextChangedListener {
            viewModel.description = it.toString().trim()
        }
        binding.typeTextNumber.addTextChangedListener {
            viewModel.type = it.toString().toIntOrNull()
        }

        viewModel.descriptionError.observe(viewLifecycleOwner) {
            if (it.isNotEmpty())
                binding.editTextTextMultiLine.error = it
        }
        viewModel.typeError.observe(viewLifecycleOwner) {
            if (it.isNotEmpty())
                binding.typeTextNumber.error = it
        }

        viewModel.imageError.observe(viewLifecycleOwner) {
            if (it.isNotEmpty())
                binding.btnPickImage.error = it
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private fun definePickImageClickListener() {
        binding.btnPickImage.setOnClickListener {
            showPickImageLoading(true)
            defineImageSelectionCallBack()
        }
    }

    private fun defineUploadButtonClickListener() {
        binding.uploadButton.setOnClickListener {
            showUploadLoading(true)
            viewModel.createPost {
                showUploadLoading(false)
                findNavController().navigate(R.id.action_new_post_to_feed)
            }
        }
    }

    @SuppressLint("Recycle")
    private fun getImageSize(uri: Uri?): Long {
        val inputStream = requireContext().contentResolver.openInputStream(uri!!)
        return inputStream?.available()?.toLong() ?: 0
    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private fun defineImageSelectionCallBack() {
        val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
        imageSelectionLauncher.launch(intent)
    }
}