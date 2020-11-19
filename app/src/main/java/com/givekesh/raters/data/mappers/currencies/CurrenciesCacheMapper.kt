package com.givekesh.raters.data.mappers.currencies

import com.givekesh.raters.data.entities.currencies.CurrenciesCacheEntity
import com.givekesh.raters.data.models.CurrenciesModel
import com.givekesh.raters.utils.EntityMapper
import javax.inject.Inject

class CurrenciesCacheMapper @Inject constructor() :
    EntityMapper<CurrenciesCacheEntity, CurrenciesModel> {
    override fun mapFromEntity(entity: CurrenciesCacheEntity): CurrenciesModel {
        return CurrenciesModel(
            title = entity.title,
            country = entity.country,
            lastUpdate = entity.lastUpdate,
            alpha2 = entity.alpha2,
            alpha3 = entity.alpha3,
            latestPrice = entity.latestPrice,
            priceChange = entity.priceChange,
            lowestPrice = entity.lowestPrice,
            highestPrice = entity.highestPrice
        )
    }

    override fun mapToEntity(domainModel: CurrenciesModel): CurrenciesCacheEntity {
        return CurrenciesCacheEntity(
            title = domainModel.title,
            country = domainModel.country,
            lastUpdate = domainModel.lastUpdate,
            alpha2 = domainModel.alpha2,
            alpha3 = domainModel.alpha3,
            latestPrice = domainModel.latestPrice,
            priceChange = domainModel.priceChange,
            lowestPrice = domainModel.lowestPrice,
            highestPrice = domainModel.highestPrice
        )
    }

    override fun mapFromEntityList(entities: List<CurrenciesCacheEntity>): List<CurrenciesModel> {
        return entities.map { mapFromEntity(it) }
    }
}