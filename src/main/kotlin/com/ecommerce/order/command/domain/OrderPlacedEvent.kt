package com.ecommerce.order.command.domain

import com.ecommerce.order.command.domain.model.Order

data class OrderPlacedEvent(
    val order: Order
)