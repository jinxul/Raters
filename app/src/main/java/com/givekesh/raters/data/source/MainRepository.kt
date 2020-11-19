package com.givekesh.raters.data.source

import com.givekesh.raters.data.mappers.RecyclerItemMapper
import com.givekesh.raters.data.mappers.coins.CoinsMapper
import com.givekesh.raters.data.mappers.currencies.CurrenciesMapper
import com.givekesh.raters.data.source.remote.NetworkApi
import com.givekesh.raters.data.mappers.coins.CoinsCacheMapper
import com.givekesh.raters.data.source.local.CoinsDao
import com.givekesh.raters.data.mappers.currencies.CurrenciesCacheMapper
import com.givekesh.raters.data.source.local.CurrenciesDao
import com.givekesh.raters.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException

class MainRepository constructor(
    private val networkApi: NetworkApi,
    private val currenciesMapper: CurrenciesMapper,
    private val coinsMapper: CoinsMapper,
    private val currenciesDao: CurrenciesDao,
    private val coinsDao: CoinsDao,
    private val currenciesCacheMapper: CurrenciesCacheMapper,
    private val coinsCacheMapper: CoinsCacheMapper,
    private val recyclerItemMapper: RecyclerItemMapper
) {
    suspend fun fetchCurrencies(): Flow<DataState> =
        flow {
            emit(DataState.Loading)
            try {
                val retrievedData = networkApi.fetchCurrencies()
                val currencies = currenciesMapper.mapFromEntityList(retrievedData.data)
                for (currency in currencies)
                    currenciesDao.insert(currenciesCacheMapper.mapToEntity(currency))
                val recyclerItems = recyclerItemMapper.mapFromCurrenciesList(currencies)
                emit(DataState.Success(data = recyclerItems, isOffline = false))
            } catch (exception: Exception) {
                if (exception is UnknownHostException) {
                    val offlineData = currenciesDao.get()
                    if (offlineData.isEmpty()) {
                        emit(DataState.Failed(exception))
                        return@flow
                    }
                    val currencies = currenciesCacheMapper.mapFromEntityList(offlineData)
                    val recyclerItems = recyclerItemMapper.mapFromCurrenciesList(currencies)
                    emit(DataState.Success(data = recyclerItems, isOffline = true))
                } else
                    emit(DataState.Failed(exception))
            }
        }

    suspend fun fetchCoins(): Flow<DataState> = flow {
        emit(DataState.Loading)
        try {
            val retrievedData = networkApi.fetchCoins()
            val coins = coinsMapper.mapFromEntityList(retrievedData.data)
            for (coin in coins)
                coinsDao.insert(coinsCacheMapper.mapToEntity(coin))
            val recyclerItems = recyclerItemMapper.mapFromCoinsList(coins)
            emit(DataState.Success(data = recyclerItems, isOffline = false))
        } catch (exception: Exception) {
            if (exception is UnknownHostException) {
                val offlineData = coinsDao.get()
                if (offlineData.isEmpty()) {
                    emit(DataState.Failed(exception))
                    return@flow
                }
                val coins = coinsCacheMapper.mapFromEntityList(offlineData)
                val recyclerItems = recyclerItemMapper.mapFromCoinsList(coins)
                emit(DataState.Success(data = recyclerItems, isOffline = true))
            } else
                emit(DataState.Failed(exception))
        }
    }
}