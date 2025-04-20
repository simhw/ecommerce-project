package com.ecommerce.order.domain

import com.ecommerce.common.model.Address
import com.ecommerce.common.model.Money
import com.ecommerce.user.domain.model.User

data class Order(
    private var id: Long,
    private val number: String,
    private var address: Address,
    private var items: List<OrderItem>,
    private var totalAmount: Money,
    private var totalDiscountedAmount: Money,
    private var orderer: User
) {
}