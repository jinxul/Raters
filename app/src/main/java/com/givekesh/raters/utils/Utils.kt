package com.givekesh.raters.utils

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import com.givekesh.raters.R
import com.givekesh.raters.ui.main.MainActivity
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class Utils(private val activity: FragmentActivity) {

    fun getErrorMessage(exception: Exception): String {
        return when (exception) {
            is UnknownHostException -> activity.getString(R.string.empty_list_error)
            is SocketTimeoutException -> activity.getString(R.string.empty_list_error)
            else -> activity.getString(R.string.unexpected_error)
        }
    }

    fun openConnectivitySettings() {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        activity.startActivity(intent)
    }

    fun isNetworkAvailable(connectivityManager: ConnectivityManager): Boolean {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val network = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            network.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            network.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    fun showThemeMenu() {
        var nightMode = (activity as MainActivity).getNightMode()
        val checkedItem = getCheckedItem(nightMode)

        AlertDialog.Builder(activity)
            .setTitle(R.string.choose_theme_title)
            .setSingleChoiceItems(R.array.theme, checkedItem) { _, item ->
                nightMode = when (item) {
                    0 -> AppCompatDelegate.MODE_NIGHT_NO
                    1 -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            }
            .setPositiveButton(R.string.confirm) { dialog, _ ->
                activity.setNightMode(nightMode)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun getCheckedItem(nightMode: Int): Int {
        return when (nightMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> 0
            AppCompatDelegate.MODE_NIGHT_YES -> 1
            else -> 2
        }
    }
}