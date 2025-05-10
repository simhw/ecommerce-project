package com.ecommerce.product.command.application.out

import com.ecommerce.product.command.domain.model.Product
import com.ecommerce.product.command.domain.model.ProductStock

interface SaveProductPort {
    fun saveProduct(product: Product)

    fun saveStock(stock: ProductStock)
}