package com.ecommerce.order.command.application.`in`

import com.ecommerce.common.model.Money
import com.ecommerce.order.command.domain.model.Order
import java.time.LocalDateTime

data class OrderInfo(
    val number: String,
    val totalAmounts: Money,
    val totalDiscountAmounts: Money,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(order: Order) = OrderInfo(
            number = order.number ?: "",
            totalAmounts = order.totalAmounts,
            totalDiscountAmounts = order.totalDiscountAmounts,
            createdAt = order.createdAt
        )
    }
}

