package com.rip.shrimptank.ui.profile

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
import com.rip.shrimptank.databinding.FragmentEditProfileBinding
import com.squareup.picasso.Picasso

class EditMyProfile : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: EditMyProfileViewModel

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
                    viewModel.imageChanged = true
                    binding.ProfileImageView.setImageURI(imageUri)
                }
            } catch (e: Exception) {
                Log.d("EditMyReview", "Error: $e")
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
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this)[EditMyProfileViewModel::class.java]

        initFields()
        defineUpdateButtonClickListener()
        definePickImageClickListener()

        viewModel.isUpdating.observe(viewLifecycleOwner) { isUpdating ->
            showUpdateLoading(isUpdating)
        }

        return view
    }

    private fun showPickImageLoading(isLoading: Boolean) {
        binding.btnPickImage.isEnabled = !isLoading
        binding.pickImageProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showUpdateLoading(isLoading: Boolean) {
        binding.updateButton.isEnabled = !isLoading
        binding.updateProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private fun definePickImageClickListener() {
        binding.btnPickImage.setOnClickListener {
            showPickImageLoading(true)
            defineImageSelectionCallBack()
        }
    }

    private fun defineUpdateButtonClickListener() {
        binding.updateButton.setOnClickListener {
            viewModel.updateUser {
                findNavController().navigate(R.id.action_edit_profile_to_profile)
            }
        }
    }

    private fun initFields() {
        viewModel.loadUser()

        binding.editTextName.addTextChangedListener {
            viewModel.name = it.toString().trim()
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.editTextName.setText(user.name)
                Picasso.get().load(user.avatar).into(binding.ProfileImageView)
            }
        }

        viewModel.nameError.observe(viewLifecycleOwner) {
            if (it.isNotEmpty())
                binding.editTextName.error = it
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
