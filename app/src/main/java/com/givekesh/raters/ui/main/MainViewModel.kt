package com.givekesh.raters.ui.main

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.*
import com.givekesh.raters.data.source.PreferenceRepository
import com.givekesh.raters.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val _nightModeLive: MutableStateFlow<Int> =
        MutableStateFlow(Constant.PREFERENCE_NIGHT_MODE_DEFAULT)
    val nightModeLive: StateFlow<Int>
        get() = _nightModeLive

    private val nightModeKey = intPreferencesKey(Constant.PREFERENCE_NIGHT_MODE_KEY)

    init {
        handleNightMode()
    }

    private fun handleNightMode() {
        viewModelScope.launch {
            preferenceRepository.observeKey(
                nightModeKey,
                Constant.PREFERENCE_NIGHT_MODE_DEFAULT
            ).collect {
                _nightModeLive.value = it
            }
        }
    }

    fun setNightMode(nightMode: Int) {
        viewModelScope.launch {
            preferenceRepository.setValue(nightModeKey, nightMode)
        }
    }
}