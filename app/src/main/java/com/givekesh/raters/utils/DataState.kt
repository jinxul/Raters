package com.givekesh.raters.utils

import com.givekesh.raters.data.models.RecyclerItemModel

sealed class DataState {
    object Idle : DataState()
    object Loading : DataState()
    class Success(val data: List<RecyclerItemModel>, val isOffline: Boolean) : DataState()
    class Failed(val exception: Exception) : DataState()
}