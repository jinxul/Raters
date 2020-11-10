package com.givekesh.raters.data.entities.currencies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
class CurrenciesCacheEntity(
    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "alpha2")
    var alpha2: String,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "alpha3")
    var alpha3: String,

    @ColumnInfo(name = "country")
    var country: String,

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