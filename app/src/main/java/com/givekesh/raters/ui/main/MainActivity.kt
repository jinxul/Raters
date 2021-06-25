package com.givekesh.raters.ui.main

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.givekesh.raters.R
import com.givekesh.raters.databinding.ActivityMainBinding
import com.givekesh.raters.databinding.DialogOfflineBinding
import com.givekesh.raters.utils.NetworkListener
import com.givekesh.raters.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    @Inject
    lateinit var networkRequest: NetworkRequest

    private var bottomSheetDialog: BottomSheetDialog? = null

    private var networkCallBack: ConnectivityManager.NetworkCallback =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                runOnUiThread {
                    if (bottomSheetDialog?.isShowing == true)
                        bottomSheetDialog?.dismiss()
                    if (shouldRefresh)
                        networkListener?.onNetworkAvailable()
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                runOnUiThread { showOfflineDialog() }
            }
        }

    lateinit var utils: Utils
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()
    private var networkListener: NetworkListener? = null
    private var shouldRefresh = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        utils = Utils(this)

        setContentView(binding.root)

        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetTheme)

        setupNavigation()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallBack)

        subscribeObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallBack)
        bottomSheetDialog?.dismiss()
    }

    override fun onResume() {
        super.onResume()
        if (!utils.isNetworkAvailable(connectivityManager))
            showOfflineDialog()
        supportActionBar?.title = navController.currentDestination?.label
    }

    private fun setupNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as
                NavHostFragment
        navController = navHost.findNavController()

        binding.navView.setupWithNavController(navController)
        setSupportActionBar(binding.toolbar)
        binding.navView.setOnNavigationItemReselectedListener { }
        binding.navView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_currencies -> {
                    if (!navController.navigateUp())
                        navController.navigate(R.id.action_coins_to_currencies)
                    supportActionBar?.title = menuItem.title
                    true
                }
                R.id.navigation_coins -> {
                    if (!navController.navigateUp())
                        navController.navigate(R.id.action_currencies_to_coins)
                    supportActionBar?.title = menuItem.title
                    true
                }
                else -> false
            }
        }
    }

    private fun subscribeObserver() {
        lifecycleScope.launch {
            viewModel.nightModeLive.collect {
                delegate.localNightMode = it
            }
        }
    }

    fun getNightMode(): Int {
        return viewModel.nightModeLive.value
    }

    fun setNightMode(nightMode: Int) {
        viewModel.setNightMode(nightMode)
    }

    private fun showOfflineDialog() {
        shouldRefresh = true
        if (bottomSheetDialog?.isShowing == true)
            return
        val sheetViewBinding = DialogOfflineBinding.inflate(layoutInflater)
        sheetViewBinding.offlineContinue.setOnClickListener { bottomSheetDialog?.dismiss() }
        sheetViewBinding.retryOnline.setOnClickListener {
            utils.openConnectivitySettings()
        }
        bottomSheetDialog?.setContentView(sheetViewBinding.root)
        bottomSheetDialog?.show()
    }

    fun registerNetworkListener(networkListener: NetworkListener) {
        this.networkListener = networkListener
    }
}