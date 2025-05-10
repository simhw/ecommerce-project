package com.ecommerce.order.command.domain.model

import com.ecommerce.common.model.Money
import com.ecommerce.product.command.domain.model.Product
import java.math.BigDecimal

class OrderLineItem(
    val id: Long? = null,
    val product: Product,
    val price: Money,
    val quantity: BigDecimal,
    var amount: Money? = Money.ZERO,
) {
    companion object {
        fun of(product: Product, quantity: BigDecimal): OrderLineItem {
            val item = OrderLineItem(
                product = product,
                price = product.price,
                quantity = quantity,
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

    /**
     * 상품 예약
     */
    fun reserveProduct() {
        product.reserve(quantity)
    }

    /**
     * 상품 취소
     */
    fun cancelProduct() {
        product.cancel(quantity)
    }

}
