package com.givekesh.raters.data.source

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.givekesh.raters.utils.Constant

class PreferenceRepository(private val sharedPreferences: SharedPreferences) {

    var nightMode: Int = Constant.PREFERENCE_NIGHT_MODE_DEFAULT
        get() = sharedPreferences.getInt(
            Constant.PREFERENCE_NIGHT_MODE_KEY,
            Constant.PREFERENCE_NIGHT_MODE_DEFAULT
        )
        set(value) {
            sharedPreferences.edit()
                .putInt(Constant.PREFERENCE_NIGHT_MODE_KEY, value)
                .apply()
            field = value
        }

    private val _nightModeLive: MutableLiveData<Int> = MutableLiveData()
    val nightModeLive: LiveData<Int>
        get() = _nightModeLive

    private val preferenceChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                Constant.PREFERENCE_NIGHT_MODE_KEY ->
                    _nightModeLive.value = nightMode
            }
        }

    init {
        _nightModeLive.value = nightMode
        sharedPreferences.registerOnSharedPreferenceChangeListener(
            preferenceChangedListener
        )
    }
}
