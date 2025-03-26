package com.rip.shrimptank.views.fragments

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.rip.shrimptank.R
import com.rip.shrimptank.databinding.FragmentRegisterBinding
import com.rip.shrimptank.interactions.FragmentChangeListener
import com.rip.shrimptank.model.Cloudinary
import com.rip.shrimptank.model.User
import com.rip.shrimptank.utils.UserInteractions
import com.rip.shrimptank.views.activities.MainActivity
import com.rip.shrimptank.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()
    private var listener: FragmentChangeListener? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private var selectedImageUri: Uri? = null

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
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
                binding.avatar.setImageURI(it)
            } ?: run {
                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.signupSigninText.setOnClickListener {
            listener?.navigateToFrag(R.id.action_registerFragment_to_loginFragment)
        }

        binding.signupBtn.setOnClickListener {
            val name = binding.name.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val confirmPassword = binding.confirmPassword.text.toString().trim()

            if (selectedImageUri == null) {
                Toast.makeText(requireContext(), "Please select a profile image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            UserInteractions.showLoading(requireActivity())

            authViewModel.validateAndRegister(
                selectedImageUri,
                name,
                email,
                password,
                confirmPassword
            ) { status, message ->
                UserInteractions.hideLoad()
                if (status) {
                    val user = User(name = name, email = email)
                    authViewModel.saveUser(user, selectedImageUri)
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.uploadImageBtn.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}