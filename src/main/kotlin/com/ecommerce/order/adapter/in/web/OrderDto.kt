package com.ecommerce.order.adapter.`in`.web

import java.time.LocalDateTime

data class OrderDto(
    val id: Long,
    val number: String,
    val totalAmount: Long,
    val totalDiscountAmount: Long,
    val createdAt: LocalDateTime
)

data class OrderItemDto(
    val name: String,
    val price: Long,
    val quantity: Long
)
