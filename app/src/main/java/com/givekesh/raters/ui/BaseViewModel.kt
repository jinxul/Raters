package com.givekesh.raters.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.givekesh.raters.data.models.RecyclerItemModel
import com.givekesh.raters.data.source.MainRepository
import com.givekesh.raters.utils.DataState
import com.givekesh.raters.utils.MainStateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
open class BaseViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _dataState: MutableLiveData<DataState<List<RecyclerItemModel>>> =
        MutableLiveData()
    val dataState: LiveData<DataState<List<RecyclerItemModel>>> = _dataState

    fun setStateEvent(mainStateEvent: MainStateEvent) {
        viewModelScope.launch {
            when (mainStateEvent) {
                is MainStateEvent.GetCurrenciesEvent -> {
                    mainRepository.fetchCurrencies()
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
                is MainStateEvent.GetCoinsEvent -> {
                    mainRepository.fetchCoins()
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }
}