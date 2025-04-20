package com.ecommerce.product.domain.model

import com.ecommerce.common.model.Money

data class Product(
    val id: Long,
    val name: String,
    val description: String,
    val price: Money
) {
}