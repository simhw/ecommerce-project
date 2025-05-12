package com.ecommerce.product.command.application.out

import com.ecommerce.product.command.domain.model.Product

interface ProductPort {
    fun loadProductBy(id: Long): Product

    fun saveProduct(product: Product)
}