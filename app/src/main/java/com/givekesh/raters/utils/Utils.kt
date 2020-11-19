package com.givekesh.raters.utils

import android.content.Context
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
}