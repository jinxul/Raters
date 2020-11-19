package com.givekesh.raters.data.mappers.coins

import com.givekesh.raters.data.entities.PricesEntity
import com.givekesh.raters.data.entities.coins.CoinsDataEntity
import com.givekesh.raters.data.models.CoinsModel
import com.givekesh.raters.utils.EntityMapper
import javax.inject.Inject

class CoinsMapper @Inject constructor() : EntityMapper<CoinsDataEntity, CoinsModel> {
    override fun mapFromEntity(entity: CoinsDataEntity): CoinsModel {
        return CoinsModel(
            title = entity.title,
            latestPrice = entity.prices.first().latestPrice,
            priceChange = entity.prices.first().priceChange,
            lowestPrice = entity.prices.first().lowestPrice,
            highestPrice = entity.prices.first().highestPrice,
            lastUpdate = entity.lastUpdate
        )
    }

    override fun mapToEntity(domainModel: CoinsModel): CoinsDataEntity {
        return CoinsDataEntity(
            title = domainModel.title,
            prices = listOf(
                PricesEntity(
                    latestPrice = domainModel.latestPrice,
                    priceChange = domainModel.priceChange,
                    lowestPrice = domainModel.lowestPrice,
                    highestPrice = domainModel.highestPrice
                )
            ),
            lastUpdate = domainModel.lastUpdate
        )
    }

    override fun mapFromEntityList(entities: List<CoinsDataEntity>): List<CoinsModel> {
        return entities.map { mapFromEntity(it) }
    }
}