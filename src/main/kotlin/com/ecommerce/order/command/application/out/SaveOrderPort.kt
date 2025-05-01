package com.ecommerce.order.command.application.out

import com.ecommerce.order.command.domain.model.Order

interface SaveOrderPort {
    fun saveOrder(order: Order)
}