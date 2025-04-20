package com.ecommerce.payment.adapter.`in`.web

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class PaymentController {

    @PostMapping("/orders/{orderId}/payment")
    fun payOrder(
        @PathVariable orderId: Long
    ) {

    }
}
