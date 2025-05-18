package com.ecommerce.product.command.adapter.out.persistence

import com.ecommerce.product.command.adapter.out.persistence.entity.ProductEntity
import com.ecommerce.product.command.adapter.out.persistence.entity.StockEntity
import com.ecommerce.product.command.domain.model.Product
import com.ecommerce.product.command.domain.model.Stock
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ProductEntityMapper {
    fun toProductEntity(domain: Product): ProductEntity

    fun toProduct(entity: ProductEntity): Product
}

@Mapper(componentModel = "spring")
interface ProductStockEntityMapper {
    fun toProductStockEntity(stock: Stock): StockEntity

    fun toProductStock(stockEntity: StockEntity): Stock
}
