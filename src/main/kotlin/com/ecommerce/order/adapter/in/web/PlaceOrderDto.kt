package com.ecommerce.order.adapter.`in`.web

object PlaceOrderDto {
    data class Request(
        val items: List<OrderItemRequest>
    )

    data class OrderItemRequest(
        val productId: Long,
        val quantity: Long
    )

    data class Response(
        val order: OrderDto,
    )
}