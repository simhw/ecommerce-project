package com.ecommerce.order.command.application.`in`

interface FailOrderUseCase {
    fun failOrder(number: String): OrderInfo
}
