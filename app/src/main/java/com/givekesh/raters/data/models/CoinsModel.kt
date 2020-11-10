package com.givekesh.raters.data.models

import com.givekesh.raters.BR
import com.givekesh.raters.R

data class CoinsModel(
    var title: String,
    var latestPrice: String,
    var priceChange: String,
    var lowestPrice: String,
    var highestPrice: String,
    var lastUpdate: String
) {
    fun toRecyclerItem() = RecyclerItemModel(
        data = this,
        layoutId = R.layout.list_item_coin,
        variableId = BR.coin
    )
}