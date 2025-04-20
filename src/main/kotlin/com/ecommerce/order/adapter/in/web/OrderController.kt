package com.ecommerce.order.adapter.`in`.web

import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/orders")
class OrderController {
    @PostMapping
    @RequestMapping("/order")
    fun orderProduct(
        @RequestBody request: PlaceOrderDto.Request
    ): PlaceOrderDto.Response = PlaceOrderDto.Response(
        OrderDto(
            1,
            "order-number",
            10000,
            1000,
            LocalDateTime.now()
        )
    )
}