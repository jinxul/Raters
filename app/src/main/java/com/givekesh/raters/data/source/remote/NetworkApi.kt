package com.givekesh.raters.data.source.remote

import com.givekesh.raters.data.entities.coins.CoinsEntity
import com.givekesh.raters.data.entities.currencies.CurrenciesEntity
import retrofit2.http.GET

interface NetworkApi {
    @GET("currency")
    suspend fun fetchCurrencies(): CurrenciesEntity

    @GET("coin/single")
    suspend fun fetchCoins(): CoinsEntity
}