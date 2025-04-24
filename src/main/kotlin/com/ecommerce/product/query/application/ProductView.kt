package com.ecommerce.product.query.application

import java.math.BigDecimal

data class ProductView(
    val id: Long,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val status: String,
    val stock: Long
)