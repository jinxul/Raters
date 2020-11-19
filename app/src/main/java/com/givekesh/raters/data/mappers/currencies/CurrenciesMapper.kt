package com.givekesh.raters.data.mappers.currencies

import com.givekesh.raters.data.entities.PricesEntity
import com.givekesh.raters.data.entities.currencies.CodesEntity
import com.givekesh.raters.data.entities.currencies.CurrenciesDataEntity
import com.givekesh.raters.data.models.CurrenciesModel
import com.givekesh.raters.utils.EntityMapper
import javax.inject.Inject

class CurrenciesMapper @Inject constructor() :
    EntityMapper<CurrenciesDataEntity, CurrenciesModel> {
    override fun mapFromEntity(entity: CurrenciesDataEntity): CurrenciesModel {
        return CurrenciesModel(
            title = entity.title,
            country = entity.country,
            alpha2 = entity.codes.first().alpha2,
            alpha3 = entity.codes.first().alpha3,
            latestPrice = entity.prices.first().latestPrice,
            priceChange = entity.prices.first().priceChange,
            lowestPrice = entity.prices.first().lowestPrice,
            highestPrice = entity.prices.first().highestPrice,
            lastUpdate = entity.lastUpdate
        )
    }

    override fun mapToEntity(domainModel: CurrenciesModel): CurrenciesDataEntity {
        return CurrenciesDataEntity(
            title = domainModel.title,
            country = domainModel.country,
            codes = listOf(
                CodesEntity(
                    alpha2 = domainModel.alpha2,
                    alpha3 = domainModel.alpha3
                )
            ),
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

    override fun mapFromEntityList(entities: List<CurrenciesDataEntity>): List<CurrenciesModel> {
        return entities.map { mapFromEntity(it) }
    }
}
