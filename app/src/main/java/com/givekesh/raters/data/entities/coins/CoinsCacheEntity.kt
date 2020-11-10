package com.givekesh.raters.data.entities.coins

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coins")
class CoinsCacheEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "latestPrice")
    var latestPrice: String,

    @ColumnInfo(name = "priceChange")
    var priceChange: String,

    @ColumnInfo(name = "lowestPrice")
    var lowestPrice: String,

    @ColumnInfo(name = "highestPrice")
    var highestPrice: String,

    @ColumnInfo(name = "lastUpdate")
    var lastUpdate: String
)