package com.ecommerce.payment.applicaiton.`in`

data class PayOrderCommand(
    val orderNumber: String,
    val userId: Long
)