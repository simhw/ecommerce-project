package com.ecommerce.product.command.adapter.out.persistence

import com.ecommerce.product.command.adapter.out.persistence.entity.StockEntity
import com.ecommerce.product.command.domain.model.Stock
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface StockEntityMapper {
    fun toStockEntity(stock: Stock): StockEntity

    fun toStock(stockEntity: StockEntity): Stock
}
