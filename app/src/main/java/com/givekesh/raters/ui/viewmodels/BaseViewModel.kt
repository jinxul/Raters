package com.givekesh.raters.ui.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.givekesh.raters.utils.DataState
import com.givekesh.raters.utils.MainIntent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
open class BaseViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val channel = Channel<MainIntent>(Channel.UNLIMITED)
    protected val mutableDataState = MutableStateFlow<DataState>(DataState.Idle)
    val dataState: StateFlow<DataState> get() = mutableDataState
}