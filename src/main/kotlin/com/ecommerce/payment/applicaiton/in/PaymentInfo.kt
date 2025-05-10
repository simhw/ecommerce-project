package com.ecommerce.payment.applicaiton.`in`

import com.ecommerce.common.model.Money
import com.ecommerce.payment.domain.model.Payment
import java.time.LocalDateTime

data class PaymentInfo(
    val amount: Money,
    val createdAt: LocalDateTime
) {
    companion object {
        fun of(payment: Payment): PaymentInfo {
            return PaymentInfo(
                amount = payment.amount,
                createdAt = payment.createdAt
            )
        }
    }
}