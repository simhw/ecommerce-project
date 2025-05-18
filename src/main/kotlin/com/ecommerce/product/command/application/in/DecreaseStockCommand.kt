package com.ecommerce.product.command.application.`in`

import java.math.BigDecimal

data class DecreaseStockCommand(
    val orderNumber: String,
    val userId: Long,
    val orderItems: List<OrderItem>
) {
    data class OrderItem(
        val productId: Long,
        val quantity: BigDecimal
    )
}
