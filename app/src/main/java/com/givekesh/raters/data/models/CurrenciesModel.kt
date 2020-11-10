package com.givekesh.raters.data.models

import com.givekesh.raters.BR
import com.givekesh.raters.R

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
) {
    fun toRecyclerItem() = RecyclerItemModel(
        data = this,
        layoutId = R.layout.list_item_currency,
        variableId = BR.currency
    )
}