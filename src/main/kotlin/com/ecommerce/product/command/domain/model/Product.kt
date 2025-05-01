package com.ecommerce.product.command.domain.model

import com.ecommerce.common.model.Money
import java.math.BigDecimal

class Product(
    val id: Long? = null,
    var name: String,
    var description: String,
    var price: Money,
    var stock: ProductStock,
    var status: ProductStatus,
) {
    fun verifyEnoughStock(count: BigDecimal) {
        if (this.stock.value < count) {
            throw IllegalArgumentException("not enough stock")
        }
    }

    fun adjustStock(delta: BigDecimal) {
        this.stock.value.plus(delta)
    }
}