package com.ecommerce.order.command.application.out

import com.ecommerce.order.command.domain.model.Order

interface LoadOrderPort {
    fun loadOrderBy(id: Long): Order

    fun loadOrderBy(number: String): Order
}