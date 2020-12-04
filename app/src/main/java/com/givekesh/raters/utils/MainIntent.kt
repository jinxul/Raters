package com.givekesh.raters.utils

sealed class MainIntent {
    object GetCurrencies : MainIntent()
    object GetCoins : MainIntent()
    object RefreshCurrencies : MainIntent()
    object RefreshCoins : MainIntent()
    object SearchCurrencies : MainIntent()
    object SearchCoins : MainIntent()
}