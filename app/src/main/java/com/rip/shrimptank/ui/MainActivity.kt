package com.rip.shrimptank.ui

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.rip.shrimptank.R
import com.rip.shrimptank.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FragmentChangeListener {

    private lateinit var context: Context
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this

        setUpToolbar()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController, AppBarConfiguration(navController.graph))
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(destination.id != R.id.homeFragment &&
                        destination.id != R.id.myPosts &&
                        destination.id != R.id.search &&
                        destination.id != R.id.explore &&
                        destination.id != R.id.profile)
                setHomeAsUpIndicator(R.drawable.ic_back_arrow)
            }
        }
    }

    private fun setUpToolbar(){
        setSupportActionBar(binding.toolbar)
        binding.toolbar.apply {
            setTitleTextColor(ContextCompat.getColor(context,R.color.white))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun navigateToFrag(fragmentId: Int, popUpId: Int) {
        navController.navigate(fragmentId)
    }
}

interface FragmentChangeListener {
    fun navigateToFrag(fragmentId: Int,popUpId:Int=0)
}