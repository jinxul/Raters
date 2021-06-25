package com.givekesh.raters.data.source

import com.givekesh.raters.data.mappers.coins.CoinsMapper
import com.givekesh.raters.data.mappers.currencies.CurrenciesMapper
import com.givekesh.raters.data.source.remote.NetworkApi
import com.givekesh.raters.data.mappers.coins.CoinsCacheMapper
import com.givekesh.raters.data.source.local.CoinsDao
import com.givekesh.raters.data.mappers.currencies.CurrenciesCacheMapper
import com.givekesh.raters.data.models.CoinsModel
import com.givekesh.raters.data.models.CurrenciesModel
import com.givekesh.raters.data.source.local.CurrenciesDao
import com.givekesh.raters.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException

class MainRepository constructor(
    private val networkApi: NetworkApi,
    private val currenciesMapper: CurrenciesMapper,
    private val coinsMapper: CoinsMapper,
    private val currenciesDao: CurrenciesDao,
    private val coinsDao: CoinsDao,
    private val currenciesCacheMapper: CurrenciesCacheMapper,
    private val coinsCacheMapper: CoinsCacheMapper
) {
    suspend fun fetchCurrencies(state: DataState<CurrenciesModel>): Flow<DataState<CurrenciesModel>> =
        flow {
            emit(state)
            try {
                val retrievedData = networkApi.fetchCurrencies()
                val currencies = currenciesMapper.mapFromEntityList(retrievedData.data)
                for (currency in currencies)
                    currenciesDao.insert(currenciesCacheMapper.mapToEntity(currency))
                emit(DataState.Success(currencies))
            } catch (exception: Exception) {
                if (exception is UnknownHostException)
                    retrieveCurrencies().collect { emit(it) }
                else
                    emit(DataState.Failed(exception))
            }
        }

    suspend fun fetchCoins(state: DataState<CoinsModel>): Flow<DataState<CoinsModel>> = flow {
        emit(state)
        try {
            val retrievedData = networkApi.fetchCoins()
            val coins = coinsMapper.mapFromEntityList(retrievedData.data)
            for (coin in coins)
                coinsDao.insert(coinsCacheMapper.mapToEntity(coin))
            emit(DataState.Success(coins))
        } catch (exception: Exception) {
            if (exception is UnknownHostException)
                retrieveCoins().collect { emit(it) }
            else
                emit(DataState.Failed(exception))
        }
    }

    suspend fun retrieveCurrencies(searchQuery: String = ""): Flow<DataState<CurrenciesModel>> =
        flow {
            val offlineData = currenciesDao.get(searchQuery)
            if (offlineData.isEmpty()) {
                emit(DataState.Failed(UnknownHostException()))
                return@flow
            }
            val currencies = currenciesCacheMapper.mapFromEntityList(offlineData)
            emit(DataState.Success(currencies))
        }

    suspend fun retrieveCoins(searchQuery: String = ""): Flow<DataState<CoinsModel>> = flow {
        val offlineData = coinsDao.get(searchQuery)
        if (offlineData.isEmpty()) {
            emit(DataState.Failed(UnknownHostException()))
            return@flow
        }
        val coins = coinsCacheMapper.mapFromEntityList(offlineData)
        emit(DataState.Success(coins))
    }
}