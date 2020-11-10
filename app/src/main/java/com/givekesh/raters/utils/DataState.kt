package com.givekesh.raters.utils

sealed class DataState<out R> {
    class Success<out T>(val data: T, val isOffline: Boolean) : DataState<T>()
    class Failed(val exception: Exception) : DataState<Nothing>()
    object Loading : DataState<Nothing>()
}