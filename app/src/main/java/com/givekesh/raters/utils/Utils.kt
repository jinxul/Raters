package com.givekesh.raters.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.givekesh.raters.R
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class Utils(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("settings", MODE_PRIVATE)

    fun getErrorMessage(exception: Exception): String {
        return when (exception) {
            is UnknownHostException -> context.getString(R.string.empty_list_error)
            is SocketTimeoutException -> context.getString(R.string.empty_list_error)
            else -> context.getString(R.string.unexpected_error)
        }
    }


    fun openConnectivitySettings() {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        context.startActivity(intent)
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
        var nightMode = getNightMode()
        val checkedItem = getCheckedItem(nightMode)

        AlertDialog.Builder(context)
            .setTitle(R.string.choose_theme_title)
            .setSingleChoiceItems(R.array.theme, checkedItem) { _, item ->
                nightMode = when (item) {
                    0 -> AppCompatDelegate.MODE_NIGHT_NO
                    1 -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            }
            .setPositiveButton(R.string.confirm) { dialog, _ ->
                sharedPreferences.edit().putInt("nightMode", nightMode).apply()
                setTheme()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun setTheme() {
        AppCompatDelegate.setDefaultNightMode(getNightMode())
    }

    private fun getNightMode(): Int {
        return sharedPreferences.getInt("nightMode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    private fun getCheckedItem(nightMode: Int): Int {
        return when (nightMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> 0
            AppCompatDelegate.MODE_NIGHT_YES -> 1
            else -> 2
        }
    }
}