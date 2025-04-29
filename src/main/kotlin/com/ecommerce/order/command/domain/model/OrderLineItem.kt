package com.ecommerce.order.command.domain.model

import com.ecommerce.common.model.Money
import com.ecommerce.product.command.domain.model.Product
import java.math.BigDecimal

class OrderLineItem(
    val id: Long?,
    val quantity: BigDecimal,
    val price: Money,
    val product: Product,
) {
    companion object {
        fun register(quantity: BigDecimal, product: Product) =
            OrderLineItem(null, quantity, product.price, product)
    }

    fun calculateAmount(): Money = price.multiply(quantity)
}
