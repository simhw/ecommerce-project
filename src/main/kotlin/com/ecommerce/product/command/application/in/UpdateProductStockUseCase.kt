package com.ecommerce.product.command.application.`in`

import com.ecommerce.product.command.domain.model.Product
import java.math.BigDecimal

interface UpdateProductStockUseCase {
    fun updateProductStock(product: Product, count: BigDecimal)
}