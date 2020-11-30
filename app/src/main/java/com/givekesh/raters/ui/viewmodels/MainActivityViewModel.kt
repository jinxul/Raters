package com.givekesh.raters.ui.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.givekesh.raters.data.source.PreferenceRepository
import com.givekesh.raters.utils.Constant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainActivityViewModel @ViewModelInject constructor(
    private val preferenceRepository: PreferenceRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val _nightModeLive: MutableStateFlow<Int> =
        MutableStateFlow(Constant.PREFERENCE_NIGHT_MODE_DEFAULT)
    val nightModeLive: StateFlow<Int>
        get() = _nightModeLive

    init {
        handleNightMode()
    }

    private fun handleNightMode() {
        viewModelScope.launch {
            preferenceRepository.observeKey(
                Constant.PREFERENCE_NIGHT_MODE_KEY,
                Constant.PREFERENCE_NIGHT_MODE_DEFAULT
            )
                .onEach { _nightModeLive.value = it }
                .launchIn(viewModelScope)
        }
    }

    fun setNightMode(nightMode: Int) {
        preferenceRepository.setValue(Constant.PREFERENCE_NIGHT_MODE_KEY, nightMode)
    }
}