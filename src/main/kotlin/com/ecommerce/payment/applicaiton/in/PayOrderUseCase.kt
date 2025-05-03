package com.ecommerce.payment.applicaiton.`in`

interface PayOrderUseCase {
    fun payOrder(command: PayOrderCommand): PaymentInfo
}