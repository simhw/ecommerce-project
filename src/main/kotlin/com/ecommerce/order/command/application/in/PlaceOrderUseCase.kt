package com.ecommerce.order.command.application.`in`

interface PlaceOrderUseCase {
    fun placeOrder(command: PlaceOrderCommand): OrderInfo
}