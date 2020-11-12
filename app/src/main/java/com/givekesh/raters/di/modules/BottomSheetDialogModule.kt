package com.givekesh.raters.di.modules

import android.content.Context
import com.givekesh.raters.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(FragmentComponent::class)
object BottomSheetDialogModule {
    @Provides
    fun provideBottomSheetDialog(@ActivityContext context: Context): BottomSheetDialog {
        return BottomSheetDialog(context, R.style.BottomSheetTheme)
    }
}