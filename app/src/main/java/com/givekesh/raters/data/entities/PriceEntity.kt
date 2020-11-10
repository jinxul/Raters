package com.givekesh.raters.data.entities

import com.google.gson.annotations.SerializedName

data class PricesEntity(
    @SerializedName("live")
    var latestPrice: String,

    @SerializedName("change")
    var priceChange: String,

    @SerializedName("min")
    var lowestPrice: String,

    @SerializedName("max")
    var highestPrice: String
)