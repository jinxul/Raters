package com.givekesh.raters.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.givekesh.raters.data.entities.coins.CoinsCacheEntity
import com.givekesh.raters.data.entities.currencies.CurrenciesCacheEntity

@Database(
    entities = [CurrenciesCacheEntity::class, CoinsCacheEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {
    abstract fun currenciesDao(): CurrenciesDao
    abstract fun coinsDao(): CoinsDao
}