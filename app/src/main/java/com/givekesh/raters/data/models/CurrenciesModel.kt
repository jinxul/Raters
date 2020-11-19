package com.givekesh.raters.data.models

data class CurrenciesModel(
    var title: String,
    var country: String,
    var lastUpdate: String,
    var alpha2: String,
    var alpha3: String,
    var latestPrice: String,
    var priceChange: String,
    var lowestPrice: String,
    var highestPrice: String
)