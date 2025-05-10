package com.ecommerce.order.query.application

import java.math.BigDecimal

data class TopSellerView(
    val productId: Long,
    val totalQuantity: BigDecimal
)