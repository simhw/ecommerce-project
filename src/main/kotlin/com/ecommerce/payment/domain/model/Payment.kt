package com.ecommerce.payment.domain.model

import com.ecommerce.order.command.domain.model.Order
import java.time.LocalDateTime

class Payment(
    val id: Long,
    val amount: Long,
    var order: Order,
    val createdAt: LocalDateTime,
) {
}