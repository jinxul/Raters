package com.givekesh.raters.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.givekesh.raters.data.entities.currencies.CurrenciesCacheEntity

@Dao
interface CurrenciesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currenciesCacheEntity: CurrenciesCacheEntity): Long

    @Query("SELECT * FROM currencies")
    suspend fun get(): List<CurrenciesCacheEntity>
}