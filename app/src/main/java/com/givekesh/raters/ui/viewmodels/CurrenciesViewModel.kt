package com.givekesh.raters.ui.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.givekesh.raters.data.source.MainRepository
import com.givekesh.raters.utils.DataState
import com.givekesh.raters.utils.MainIntent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class CurrenciesViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val channel = Channel<MainIntent>(Channel.UNLIMITED)
    private val _dataState = MutableStateFlow<DataState>(DataState.Idle)
    val dataState: StateFlow<DataState> get() = _dataState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            channel.consumeAsFlow().collect { mainIntent ->
                when (mainIntent) {
                    MainIntent.GetCurrencies ->
                        mainRepository.fetchCurrencies(DataState.Loading)
                            .onEach { _dataState.value = it }
                            .launchIn(viewModelScope)
                    MainIntent.RefreshCurrencies ->
                        mainRepository.fetchCurrencies(DataState.Refreshing)
                            .onEach { _dataState.value = it }
                            .launchIn(viewModelScope)
                    else -> Unit
                }
            }
        }
    }
}