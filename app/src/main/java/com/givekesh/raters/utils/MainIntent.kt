package com.givekesh.raters.utils

sealed class MainIntent {
    object GetCurrencies : MainIntent()
    object GetCoins : MainIntent()
    object RefreshCurrencies : MainIntent()
    object RefreshCoins : MainIntent()
    class SearchCurrencies(val searchQuery: String) : MainIntent()
    class SearchCoins(val searchQuery: String) : MainIntent()
}