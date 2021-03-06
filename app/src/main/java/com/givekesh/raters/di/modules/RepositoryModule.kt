package com.givekesh.raters.di.modules

import android.content.Context
import com.givekesh.raters.data.mappers.coins.CoinsMapper
import com.givekesh.raters.data.mappers.currencies.CurrenciesMapper
import com.givekesh.raters.data.source.MainRepository
import com.givekesh.raters.data.source.remote.NetworkApi
import com.givekesh.raters.data.mappers.coins.CoinsCacheMapper
import com.givekesh.raters.data.source.local.CoinsDao
import com.givekesh.raters.data.mappers.currencies.CurrenciesCacheMapper
import com.givekesh.raters.data.source.PreferenceRepository
import com.givekesh.raters.data.source.local.CurrenciesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideMainRepository(
        networkApi: NetworkApi,
        currenciesMapper: CurrenciesMapper,
        coinsMapper: CoinsMapper,
        currenciesDao: CurrenciesDao,
        coinsDao: CoinsDao,
        currenciesCacheMapper: CurrenciesCacheMapper,
        coinsCacheMapper: CoinsCacheMapper
    ): MainRepository {
        return MainRepository(
            networkApi,
            currenciesMapper,
            coinsMapper,
            currenciesDao,
            coinsDao,
            currenciesCacheMapper,
            coinsCacheMapper
        )
    }

    @Singleton
    @Provides
    fun providePreferenceRepository(
        @ApplicationContext context: Context
    ): PreferenceRepository {
        return PreferenceRepository(context)
    }
}