package com.ecommerce.payment.adapter.`in`.web

import java.math.BigDecimal
import java.time.LocalDateTime

data class PaymentDto(
    val amount: BigDecimal,
    val createdAt: LocalDateTime
)