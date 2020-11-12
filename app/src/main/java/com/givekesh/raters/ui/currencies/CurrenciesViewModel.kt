package com.givekesh.raters.ui.currencies

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.givekesh.raters.data.source.MainRepository
import com.givekesh.raters.ui.BaseViewModel
import com.givekesh.raters.utils.MainIntent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class CurrenciesViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : BaseViewModel(savedStateHandle) {

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            channel.consumeAsFlow().collect { mainIntent ->
                if (mainIntent is MainIntent.GetCurrencies) {
                    mainRepository.fetchCurrencies()
                        .onEach { dataState ->
                            mutableDataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }
}