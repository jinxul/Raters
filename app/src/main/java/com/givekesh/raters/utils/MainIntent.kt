package com.givekesh.raters.utils

sealed class MainIntent {
    object GetCurrencies : MainIntent()
    object GetCoins : MainIntent()
}