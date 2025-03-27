package com.rip.shrimptank.views.activities

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.rip.shrimptank.R
import com.rip.shrimptank.databinding.ActivityAuthBinding
import com.rip.shrimptank.interactions.FragmentChangeListener
import com.rip.shrimptank.viewmodel.AuthViewModel
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(), FragmentChangeListener {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var context: Context
    private var navController: NavController?=null
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Step 1: Check if the user is logged in
        if (authViewModel.checkAuth()) {
            // Step 2: Try to load user data from Firestore
            authViewModel.checkIfUserLoggedIn { user ->
                if (user != null) {
                    com.rip.shrimptank.utils.UserInteractions.userData = user
                    // Step 3: Navigate to MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // Could not fetch user details, fallback to auth screen
                    loadAuthUI()
                }
            }
        } else {
            // No user logged in, show auth screen
            loadAuthUI()
        }
    }

    override fun onNavigateUp(): Boolean {
        return navController!!.navigateUp() || super.onSupportNavigateUp()
    }

    override fun navigateToFrag(fragmentId: Int, popUpId: Int) {
        navController!!.navigate(fragmentId)
    }

    private fun loadAuthUI() {
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_auth) as NavHostFragment

        navController = navHostFragment.navController
    }

}