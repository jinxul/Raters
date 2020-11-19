package com.givekesh.raters.data.mappers.coins

import com.givekesh.raters.data.entities.coins.CoinsCacheEntity
import com.givekesh.raters.data.models.CoinsModel
import com.givekesh.raters.utils.EntityMapper
import javax.inject.Inject

class CoinsCacheMapper @Inject constructor() :
    EntityMapper<CoinsCacheEntity, CoinsModel> {
    override fun mapFromEntity(entity: CoinsCacheEntity): CoinsModel {
        return CoinsModel(
            title = entity.title,
            latestPrice = entity.latestPrice,
            priceChange = entity.priceChange,
            lowestPrice = entity.lowestPrice,
            highestPrice = entity.highestPrice,
            lastUpdate = entity.lastUpdate
        )
    }

    override fun mapToEntity(domainModel: CoinsModel): CoinsCacheEntity {
        return CoinsCacheEntity(
            title = domainModel.title,
            latestPrice = domainModel.latestPrice,
            priceChange = domainModel.priceChange,
            lowestPrice = domainModel.lowestPrice,
            highestPrice = domainModel.highestPrice,
            lastUpdate = domainModel.lastUpdate
        )
    }

    override fun mapFromEntityList(entities: List<CoinsCacheEntity>): List<CoinsModel> {
        return entities.map { mapFromEntity(it) }
    }
}