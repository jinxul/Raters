package com.givekesh.raters.di.modules

import android.content.Context
import android.content.res.TypedArray
import com.givekesh.raters.R
import com.givekesh.raters.di.components.BindingAdaptersComponent
import com.givekesh.raters.di.scopes.BindingAdaptersScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(BindingAdaptersComponent::class)
object BindingAdapterModule {
    @BindingAdaptersScope
    @Provides
    fun provideAlpha2StringArray(@ApplicationContext context: Context): Array<String> =
        context.resources.getStringArray(R.array.flag_alpha2)

    @BindingAdaptersScope
    @Provides
    fun provideAlpha2TypedArray(@ApplicationContext context: Context): TypedArray =
        context.resources.obtainTypedArray(R.array.flag_drawable)
}