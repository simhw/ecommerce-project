package com.ecommerce.product.command.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

class ProductStock(
    val id: Long? = null,
    var value: BigDecimal,
    val updatedAt: LocalDateTime,
)