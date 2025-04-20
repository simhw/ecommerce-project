package com.ecommerce.order.domain

import com.ecommerce.product.domain.model.Product

class OrderItem(
    id: Long,
    quantity: Int,
    amount: Long,
    product: Product,
) {
}
