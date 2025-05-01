package com.ecommerce.order.command.domain.model

import com.ecommerce.common.model.Money
import com.ecommerce.product.command.domain.model.Product
import java.math.BigDecimal

class OrderLineItem(
    val id: Long? = null,
    val price: Money,
    val quantity: BigDecimal,
    var amount: Money? = Money.ZERO,
    val product: Product
) {
    companion object {
        fun of(quantity: BigDecimal, product: Product): OrderLineItem {
            val item = OrderLineItem(
                null,
                price = product.price,
                quantity = quantity,
                product = product
            )
            item.amount = item.calculateAmount()
            return item
        }
    }

    fun calculateAmount(): Money = price.multiply(quantity)
}
