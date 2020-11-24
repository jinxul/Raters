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
import com.givekesh.raters.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_offline.*
import kotlinx.android.synthetic.main.dialog_offline.view.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetTheme)

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
        val sheetView = layoutInflater.inflate(R.layout.dialog_offline, bottom_sheet)
        sheetView.offline_continue.setOnClickListener { bottomSheetDialog?.dismiss() }
        sheetView.retry_online.setOnClickListener {
            utils.openConnectivitySettings(this)
        }
        bottomSheetDialog?.setContentView(sheetView)
        bottomSheetDialog?.show()
    }
}