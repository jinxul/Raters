package com.givekesh.raters.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*

class PreferenceRepository(val context: Context) {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

    inline fun <reified T> observeKey(
        key: Preferences.Key<T>,
        default: T
    ): Flow<T> = channelFlow {
        context.dataStore.data.collect { preferences ->
            trySend(preferences[key] ?: default)
        }
    }

    suspend inline fun <reified T> setValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}
