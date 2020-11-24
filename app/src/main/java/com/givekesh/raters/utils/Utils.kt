package com.givekesh.raters.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import com.givekesh.raters.R
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class Utils {
    fun getErrorMessage(context: Context, exception: Exception): String {
        return when (exception) {
            is UnknownHostException -> context.getString(R.string.empty_list_error)
            is SocketTimeoutException -> context.getString(R.string.empty_list_error)
            else -> context.getString(R.string.unexpected_error)
        }
    }


    fun openConnectivitySettings(context: Context) {
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
}