package com.givekesh.raters.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.givekesh.raters.data.source.MainRepository
import com.givekesh.raters.utils.DataState
import com.givekesh.raters.utils.MainIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class CoinsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mainRepository: MainRepository
) : ViewModel() {

    val channel = Channel<MainIntent>(Channel.UNLIMITED)
    private val _dataState = MutableStateFlow<DataState>(DataState.Idle)
    val dataState: StateFlow<DataState> get() = _dataState

    var searchQuery: String? = savedStateHandle.get<String>("searchQuery")

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            channel.consumeAsFlow().collect { mainIntent ->
                when (mainIntent) {
                    MainIntent.GetCoins ->
                        mainRepository.fetchCoins(DataState.Loading)
                            .onEach { _dataState.value = it }
                            .launchIn(viewModelScope)
                    MainIntent.RefreshCoins ->
                        mainRepository.fetchCoins(DataState.Refreshing)
                            .onEach { _dataState.value = it }
                            .launchIn(viewModelScope)
                    is MainIntent.SearchCoins -> {
                        searchQuery = mainIntent.searchQuery
                        mainRepository
                            .retrieveCoins(searchQuery ?: "")
                            .onEach { _dataState.value = it }
                            .launchIn(viewModelScope)
                    }
                    else -> Unit
                }
            }
        }
    }
}