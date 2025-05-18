package com.ecommerce.order.command.domain.model

import com.ecommerce.common.model.Money
import java.math.BigDecimal

class OrderLineItem(
    val id: Long? = null,
    val productId: Long,
    val price: Money,
    val quantity: BigDecimal,
    var amount: Money? = Money.ZERO
) {
    companion object {
        fun of(productId: Long, price: Money, quantity: BigDecimal): OrderLineItem {
            val item = OrderLineItem(
                productId = productId,
                price = price,
                quantity = quantity
            )
            item.amount = item.calculateAmount()
            return item
        }
    }

    /**
     * 주문 금액 계산
     */
    fun calculateAmount(): Money {
        return price.multiply(quantity)
    }
}
