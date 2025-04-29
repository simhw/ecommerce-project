package com.ecommerce.product.command.application.out

import com.ecommerce.product.command.domain.model.Product

interface SaveProductPort {
    fun saveProduct(product: Product)

    fun saveAllProducts(products: List<Product>)
}