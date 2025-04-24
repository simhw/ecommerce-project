package com.ecommerce.product.command.domain.model

import com.ecommerce.common.model.Money

class Product(
    val id: Long? = null,
    var name: String,
    var description: String,
    var price: Money,
    var stock: ProductStock,
    var status: ProductStatus,
)