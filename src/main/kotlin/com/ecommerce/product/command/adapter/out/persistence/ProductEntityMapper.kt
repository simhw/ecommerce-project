package com.ecommerce.product.command.adapter.out.persistence

import com.ecommerce.product.command.adapter.out.persistence.entity.ProductEntity
import com.ecommerce.product.command.adapter.out.persistence.entity.ProductStockEntity
import com.ecommerce.product.command.domain.model.Product
import com.ecommerce.product.command.domain.model.ProductStock
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(
    componentModel = "spring",
    uses = [ProductStockEntityMapper::class]
)
interface ProductEntityMapper {
    @Mapping(target = "stock", ignore = true)
    fun toProductEntity(domain: Product): ProductEntity

    fun toProduct(entity: ProductEntity): Product
}

@Mapper(componentModel = "spring")
interface ProductStockEntityMapper {
    @Mapping(target = "product", ignore = true)
    fun toProductStockEntity(productStock: ProductStock): ProductStockEntity

    fun toProductStock(productStockEntity: ProductStockEntity): ProductStock
}
