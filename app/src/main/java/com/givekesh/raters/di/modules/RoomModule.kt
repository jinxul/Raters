package com.givekesh.raters.di.modules

import android.content.Context
import androidx.room.Room
import com.givekesh.raters.data.source.local.MainDatabase
import com.givekesh.raters.data.source.local.CoinsDao
import com.givekesh.raters.data.source.local.CurrenciesDao
import com.givekesh.raters.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun provideMainDatabase(@ApplicationContext context: Context): MainDatabase {
        return Room
            .databaseBuilder(
                context,
                MainDatabase::class.java,
                Constant.DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideCurrenciesDao(db: MainDatabase): CurrenciesDao {
        return db.currenciesDao()
    }

    @Singleton
    @Provides
    fun provideCoinsDao(db: MainDatabase): CoinsDao {
        return db.coinsDao()
    }
}