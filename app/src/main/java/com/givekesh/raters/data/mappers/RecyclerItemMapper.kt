package com.givekesh.raters.data.mappers

import com.givekesh.raters.BR
import com.givekesh.raters.R
import com.givekesh.raters.data.models.CoinsModel
import com.givekesh.raters.data.models.CurrenciesModel
import com.givekesh.raters.data.models.RecyclerItemModel
import javax.inject.Inject

class RecyclerItemMapper @Inject constructor() {
    private fun mapFromCurrencies(currency: CurrenciesModel): RecyclerItemModel {
        return RecyclerItemModel(
            data = currency,
            layoutId = R.layout.list_item_currency,
            variableId = BR.currency
        )
    }

    private fun mapFromCoins(coin: CoinsModel): RecyclerItemModel {
        return RecyclerItemModel(
            data = coin,
            layoutId = R.layout.list_item_coin,
            variableId = BR.coin
        )
    }

    fun mapFromCurrenciesList(currencies: List<CurrenciesModel>): List<RecyclerItemModel> {
        return currencies.map { mapFromCurrencies(it) }
    }

    fun mapFromCoinsList(coins: List<CoinsModel>): List<RecyclerItemModel> {
        return coins.map { mapFromCoins(it) }
    }
}