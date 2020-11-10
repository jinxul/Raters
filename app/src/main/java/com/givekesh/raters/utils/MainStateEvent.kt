package com.givekesh.raters.utils

sealed class MainStateEvent {
    object GetCurrenciesEvent : MainStateEvent()
    object GetCoinsEvent : MainStateEvent()
}