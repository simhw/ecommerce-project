package com.ecommerce.product.command.application.out

import com.ecommerce.product.command.domain.model.Product

interface LoadProductPort {
    fun loadProductBy(id: Long): Product
}