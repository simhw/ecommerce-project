package com.ecommerce.order.command.adapter.`in`.web

import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderDto(
    val number: String,
    val totalAmount: BigDecimal,
    val totalDiscountAmount: BigDecimal,
    val createdAt: LocalDateTime
)

data class OrderItemDto(
    val name: String,
    val price: Long,
    val quantity: Long
)
