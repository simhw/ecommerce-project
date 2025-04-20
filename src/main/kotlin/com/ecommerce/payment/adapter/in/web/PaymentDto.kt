package com.ecommerce.payment.adapter.`in`.web

import com.ecommerce.order.adapter.`in`.web.OrderDto

data class PaymentDto(
    val id: Long,
    val amount: Long,
    val order: OrderDto
)