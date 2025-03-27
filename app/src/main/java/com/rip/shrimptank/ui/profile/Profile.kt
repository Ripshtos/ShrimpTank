package com.rip.shrimptank.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.rip.shrimptank.R
import com.rip.shrimptank.ui.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.rip.shrimptank.model.user.UserModel
import com.squareup.picasso.Picasso

class Profile : Fragment() {

    private lateinit var root: View
    private var auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_profile, container, false)

        setUserNameTextView()
        setProfileImage()

        root.findViewById<Button>(R.id.EditMyProfileButton).setOnClickListener {
            Navigation.findNavController(root).navigate(R.id.action_profile_to_editMyProfile)
        }
        root.findViewById<Button>(R.id.LogOutButton).setOnClickListener {
            logOutUser()
        }
        return root
    }

    private fun setProfileImage() {
        UserModel.instance.getCurrentUser().observe(viewLifecycleOwner) { user ->
            if (user != null) {
                Picasso.get().load(user.avatar).into(root.findViewById<ImageView>(R.id.ProfileImageView))
            }
        }
    }

    private fun setUserNameTextView() {
        root.findViewById<TextView>(R.id.UserNameTextView).text = "${auth.currentUser?.displayName}"
    }

    private fun logOutUser() {
        auth.signOut()
        Toast.makeText(
            requireContext(),
            "Logged out, Bye!",
            Toast.LENGTH_SHORT
        ).show()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }
}