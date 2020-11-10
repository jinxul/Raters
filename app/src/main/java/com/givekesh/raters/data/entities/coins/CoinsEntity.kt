package com.givekesh.raters.data.entities.coins

import com.givekesh.raters.data.entities.PricesEntity
import com.google.gson.annotations.SerializedName

data class CoinsEntity(
    @SerializedName("data")
    var data: List<CoinsDataEntity>
)

data class CoinsDataEntity(
    @SerializedName("title")
    var title: String,

    @SerializedName("prices")
    var prices: List<PricesEntity>,

    @SerializedName("time")
    var lastUpdate: String
)