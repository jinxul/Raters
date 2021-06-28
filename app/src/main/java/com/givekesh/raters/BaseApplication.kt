package com.givekesh.raters

import android.app.Application
import androidx.databinding.DataBindingUtil
import com.givekesh.raters.di.components.BindingAdaptersComponentBuilder
import com.givekesh.raters.di.entrypoints.BindingAdaptersEntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class BaseApplication : Application() {

    @Inject
    lateinit var bindingComponentProvider: Provider<BindingAdaptersComponentBuilder>

    override fun onCreate() {
        super.onCreate()
        val dataBindingComponent = bindingComponentProvider.get().build()
        val dataBindingEntryPoint = EntryPoints.get(
            dataBindingComponent, BindingAdaptersEntryPoint::class.java
        )
        DataBindingUtil.setDefaultComponent(dataBindingEntryPoint)
    }
}
