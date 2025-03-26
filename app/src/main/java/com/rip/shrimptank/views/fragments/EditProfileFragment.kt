package com.rip.shrimptank.views.fragments

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rip.shrimptank.R
import com.rip.shrimptank.databinding.FragmentEditProfileBinding
import com.rip.shrimptank.interactions.FragmentChangeListener
import com.rip.shrimptank.model.Cloudinary
import com.rip.shrimptank.model.User
import com.rip.shrimptank.utils.UserInteractions
import com.rip.shrimptank.viewmodel.AuthViewModel
import com.rip.shrimptank.views.activities.MainActivity
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val authViewModel: AuthViewModel by viewModels()
    private var listener: FragmentChangeListener? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private var selectedImageUri: Uri? = null
    private lateinit var user: User

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement FragmentChangeListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = UserInteractions.userData!!
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                binding.profileImage.setImageURI(uri)
            } else {
                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        super.onCreate(savedInstanceState)
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                binding.profileImage.setImageURI(uri)
            }
            else {
                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            }
        }
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.uploadImageBtn.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        Picasso.get().load(user?.avatar)
            .placeholder(R.drawable.loader)
            .error(R.drawable.placeholder)
            .resize(200,200)
            .centerCrop()
            .into(binding.profileImage)
        binding.name.setText(user?.name)

        binding.updateBtn.setOnClickListener {
            val name = binding.name.text.toString().trim()

            binding.profileImage.isDrawingCacheEnabled = true
            binding.profileImage.buildDrawingCache()
            val bitmap = (binding.profileImage.drawable as BitmapDrawable).bitmap

            if (selectedImageUri != null) {
                Cloudinary.shared.add(user, bitmap, Cloudinary.Storage.CLOUDINARY) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "User updated successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
            }

            if (name.isNotEmpty()) {
                UserInteractions.showLoading(requireActivity())
                user?.name = name
                authViewModel.updateUser(
                    user!!, selectedImageUri
                ) { status, message ->
                    UserInteractions.hideLoad()
                    if (status) {
                        UserInteractions.userData = user
                        UserInteractions.showDlg(requireActivity(), message!!
                        ) { p0, p1 -> listener?.navigateToFrag(R.id.action_editProfile_to_profile) }
                    } else {
                        UserInteractions.showDlg(requireActivity(), message!!)
                    }
                }
            }
            else {
                UserInteractions.showDlg(requireActivity(), "Name input required!")
            }




        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}