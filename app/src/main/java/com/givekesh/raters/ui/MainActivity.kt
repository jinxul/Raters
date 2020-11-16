package com.givekesh.raters.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.givekesh.raters.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as
                NavHostFragment
        val navController = navHost.findNavController()

        nav_view.setupWithNavController(navController)
        setSupportActionBar(toolbar)
        nav_view.setOnNavigationItemReselectedListener { }
        nav_view.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_currencies -> {
                    navController.navigate(R.id.navigation_currencies)
                    supportActionBar?.title = menuItem.title
                    true
                }
                R.id.navigation_coins -> {
                    navController.navigate(R.id.navigation_coins)
                    supportActionBar?.title = menuItem.title
                    true
                }
                else -> false
            }
        }
    }
}