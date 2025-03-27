package com.rip.shrimptank.views.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.rip.shrimptank.R
import com.rip.shrimptank.databinding.FragmentProfileBinding
import com.rip.shrimptank.interactions.FragmentChangeListener
import com.rip.shrimptank.model.User
import com.rip.shrimptank.utils.UserInteractions
import com.rip.shrimptank.views.activities.AuthActivity
import com.rip.shrimptank.viewmodel.AuthViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val authViewModel: AuthViewModel by viewModels()
    private var listener: FragmentChangeListener? = null
    private var user: User? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement FragmentChangeListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show loading state
        binding.profileDetailWrapperLayout.visibility = View.VISIBLE
        binding.loadingSpinner.visibility = View.VISIBLE
        binding.name.setText("Loading...")
        binding.email.setText("Loading...")

        val user = UserInteractions.userData

        if (user != null) {
            loadUserIntoUI(user)
        } else {
            val userId = authViewModel.getid()
            authViewModel.fetchUserDetails(userId) { fetchedUser ->
                if (fetchedUser != null) {
                    UserInteractions.userData = fetchedUser
                    loadUserIntoUI(fetchedUser)
                } else {
                    binding.name.setText("Failed to load name")
                    binding.email.setText("Failed to load email")
                    binding.loadingSpinner.visibility = View.GONE
                }
            }
        }

        binding.editBtn.setOnClickListener {
            listener?.navigateToFrag(R.id.action_profile_to_editProfile, R.id.profileFragment)
        }

        binding.logoutBtn.setOnClickListener {
            authViewModel.logout()
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun loadUserIntoUI(user: User) {
        Picasso.get()
            .load(user.avatar)
            .placeholder(R.drawable.loader)
            .error(R.drawable.placeholder)
            .resize(200, 200)
            .centerCrop()
            .into(binding.profileImage)

        binding.name.setText(user.name)
        binding.email.setText(user.email)

        binding.loadingSpinner.visibility = View.GONE
    }

}