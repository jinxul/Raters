package com.givekesh.raters.ui

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.givekesh.raters.R
import com.givekesh.raters.databinding.ActivityMainBinding
import com.givekesh.raters.databinding.DialogOfflineBinding
import com.givekesh.raters.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
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
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                runOnUiThread { showOfflineDialog() }
            }
        }

    private val utils = Utils()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetTheme)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as
                NavHostFragment
        val navController = navHost.findNavController()

        binding.navView.setupWithNavController(navController)
        setSupportActionBar(binding.toolbar)
        binding.navView.setOnNavigationItemReselectedListener { }
        binding.navView.setOnNavigationItemSelectedListener { menuItem ->
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

        connectivityManager.registerNetworkCallback(networkRequest, networkCallBack)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallBack)
    }

    override fun onResume() {
        super.onResume()
        if (!utils.isNetworkAvailable(connectivityManager))
            showOfflineDialog()
    }

    fun showOfflineDialog() {
//        val sheetView = layoutInflater.inflate(R.layout.dialog_offline, bottom_sheet)
        val sheetViewBinding = DialogOfflineBinding.inflate(layoutInflater)
        sheetViewBinding.offlineContinue.setOnClickListener { bottomSheetDialog?.dismiss() }
        sheetViewBinding.retryOnline.setOnClickListener {
            utils.openConnectivitySettings(this)
        }
        bottomSheetDialog?.setContentView(sheetViewBinding.root)
        bottomSheetDialog?.show()
    }
}