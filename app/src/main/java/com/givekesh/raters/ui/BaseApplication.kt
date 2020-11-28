package com.givekesh.raters.ui

import android.app.Application
import com.givekesh.raters.utils.Utils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Utils(this).setTheme()
    }
}