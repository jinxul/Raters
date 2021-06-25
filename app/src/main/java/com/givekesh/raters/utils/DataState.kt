package com.givekesh.raters.utils

sealed class DataState<out R> {
    object Idle : DataState<Nothing>()
    object Loading : DataState<Nothing>()
    object Refreshing : DataState<Nothing>()
    class Success<T>(val data: List<T>) : DataState<T>()
    class Failed(val exception: Exception) : DataState<Nothing>()
}