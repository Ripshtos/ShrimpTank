package com.rip.shrimptank.views.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.rip.shrimptank.R
import com.rip.shrimptank.databinding.ActivityAuthBinding
import com.rip.shrimptank.interactions.FragmentChangeListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(), FragmentChangeListener {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var context: Context
    private var navController: NavController?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_auth) as NavHostFragment

        navController = navHostFragment.navController

    }

    override fun onNavigateUp(): Boolean {
        return navController!!.navigateUp() || super.onSupportNavigateUp()
    }

    override fun navigateToFrag(fragmentId: Int, popUpId: Int) {
        navController!!.navigate(fragmentId)
    }
}