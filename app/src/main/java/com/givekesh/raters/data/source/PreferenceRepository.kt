package com.givekesh.raters.data.source

import android.content.SharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

@ExperimentalCoroutinesApi
class PreferenceRepository(val sharedPreferences: SharedPreferences) {

    inline fun <reified T> observeKey(
        key: String,
        default: T
    ): Flow<T> = channelFlow {
        offer(getItem(key, default))

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, k ->
            if (key == k) {
                offer(getItem(key, default)!!)
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    inline fun <reified T> getItem(key: String, default: T): T {
        @Suppress("UNCHECKED_CAST")
        return when (default) {
            is String -> sharedPreferences.getString(key, default) as T
            is Int -> sharedPreferences.getInt(key, default) as T
            is Long -> sharedPreferences.getLong(key, default) as T
            is Boolean -> sharedPreferences.getBoolean(key, default) as T
            is Float -> sharedPreferences.getFloat(key, default) as T
            else -> throw IllegalArgumentException("generic type not handle ${T::class.java.name}")
        }
    }

    inline fun <reified T> setValue(key: String, value: T) {
        when (value) {
            is String -> sharedPreferences.edit().putString(key, value).apply()
            is Int -> sharedPreferences.edit().putInt(key, value).apply()
            is Long -> sharedPreferences.edit().putLong(key, value).apply()
            is Boolean -> sharedPreferences.edit().putBoolean(key, value).apply()
            is Float -> sharedPreferences.edit().putFloat(key, value).apply()
            else -> throw IllegalArgumentException("generic type not handle ${T::class.java.name}")
        }
    }
}
