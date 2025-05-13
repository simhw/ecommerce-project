package com.ecommerce.product.command.domain.model

import com.ecommerce.common.model.Money

class Product(
    val id: Long? = null,
    var name: String,
    var description: String,
    var price: Money,
    var status: ProductStatus
) {

    fun getIdOrThrow() = id ?: throw IllegalStateException("product id $id not found")

    fun verifyAvailableForSale() {
        if (status != ProductStatus.SELL) {
            throw IllegalArgumentException("is not available for sale")
        }
    }
}