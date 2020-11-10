package com.givekesh.raters.ui.coins

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.givekesh.raters.data.source.MainRepository
import com.givekesh.raters.ui.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CoinsViewModel @ViewModelInject constructor(
    mainRepository: MainRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : BaseViewModel(mainRepository, savedStateHandle)