package com.ecommerce.product.command.adapter.out.persistence

import com.ecommerce.product.command.domain.model.Product
import com.ecommerce.product.command.domain.model.ProductStock
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring",
    uses = [ProductStockEntityMapper::class, ])
interface ProductEntityMapper {
    fun toProduct(entity: ProductEntity): Product

    fun toProductEntity(domain: Product): ProductEntity
}

@Mapper(componentModel = "spring")
interface ProductStockEntityMapper {
    // circular reference
    @Mapping(target = "product", ignore = true)
    fun toProductStockEntity(domain: ProductStock): ProductStockEntity

    fun toProductStock(entity: ProductStockEntity): ProductStock
}

//@Mapper(componentModel = "spring")
//interface MoneyMapper {
//    @Named("toMoney")
//    fun toMoney(amount: BigDecimal): Money = Money(amount)
//
//    @Named("toBigDecimal")
//    fun toBigDecimal(money: Money): BigDecimal = money.amount
//}
