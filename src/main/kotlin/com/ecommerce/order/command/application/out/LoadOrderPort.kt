package com.ecommerce.order.command.application.out

import com.ecommerce.order.command.domain.model.Order

interface LoadOrderPort {
    fun loadOrderById(id: Long): Order

    fun loadOrderByNumber(number: String): Order
}