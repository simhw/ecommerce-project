package com.ecommerce.order.command.application.`in`

import com.ecommerce.common.model.Address
import java.math.BigDecimal

data class PlaceOrderCommand(
    val userId: Long,
    val address: Address,
    val userCouponId: Long? = null,
    val items: List<OrderItem>
) {
    data class OrderItem(
        val productId: Long,
        val quantity: BigDecimal
    )
}
