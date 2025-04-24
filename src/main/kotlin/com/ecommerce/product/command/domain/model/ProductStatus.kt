package com.ecommerce.product.command.domain.model

enum class ProductStatus(
    val description: String
) {
    SELL("판매중"),
    SOLD_OUT("품절")
}
