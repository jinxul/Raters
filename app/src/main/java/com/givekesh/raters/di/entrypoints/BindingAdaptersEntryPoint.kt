package com.givekesh.raters.di.entrypoints

import androidx.databinding.DataBindingComponent
import com.givekesh.raters.databinding.BindingAdapters
import com.givekesh.raters.di.components.BindingAdaptersComponent
import com.givekesh.raters.di.scopes.BindingAdaptersScope
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn

@EntryPoint
@BindingAdaptersScope
@InstallIn(BindingAdaptersComponent::class)
interface BindingAdaptersEntryPoint : DataBindingComponent {
    override fun getBindingAdapters(): BindingAdapters
}