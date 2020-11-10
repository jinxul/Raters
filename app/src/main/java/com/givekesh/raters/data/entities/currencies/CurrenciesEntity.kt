package com.givekesh.raters.data.entities.currencies

import com.givekesh.raters.data.entities.PricesEntity
import com.google.gson.annotations.SerializedName

data class CurrenciesEntity(
    @SerializedName("data")
    var data: List<CurrenciesDataEntity>
)

data class CurrenciesDataEntity(
    @SerializedName("title")
    var title: String,

    @SerializedName("codes")
    var codes: List<CodesEntity>,

    @SerializedName("country")
    var country: String,

    @SerializedName("prices")
    var prices: List<PricesEntity>,

    @SerializedName("time")
    var lastUpdate: String
)

data class CodesEntity(
    @SerializedName("alpha2")
    var alpha2: String,

    @SerializedName("alpha3")
    var alpha3: String
)