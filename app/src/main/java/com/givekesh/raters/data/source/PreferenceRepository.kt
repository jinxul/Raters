package com.givekesh.raters.data.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class PreferenceRepository(val dataStore: DataStore<Preferences>) {

    inline fun <reified T> observeKey(
        key: Preferences.Key<T>,
        default: T
    ): Flow<T> = channelFlow {
        dataStore.data.collect { preferences ->
            offer(preferences[key] ?: default)
        }
    }

    suspend inline fun <reified T> setValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}
