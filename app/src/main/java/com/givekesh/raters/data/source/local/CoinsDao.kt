package com.givekesh.raters.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.givekesh.raters.data.entities.coins.CoinsCacheEntity

@Dao
interface CoinsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(coinsCacheEntity: CoinsCacheEntity): Long

    @Query("SELECT * FROM coins")
    suspend fun get(): List<CoinsCacheEntity>
}