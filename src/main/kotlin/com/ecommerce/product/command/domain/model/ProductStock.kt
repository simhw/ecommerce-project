package com.ecommerce.product.command.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

class ProductStock(
    val id: Long? = null,
    val value: BigDecimal,
    val product: Product,
    val updatedAt: LocalDateTime,
) {
}