package com.ecommerce.payment.adapter.`in`.web

import com.ecommerce.payment.applicaiton.`in`.PayOrderCommand
import com.ecommerce.payment.applicaiton.`in`.PayOrderUseCase
import com.ecommerce.payment.applicaiton.`in`.PaymentInfo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class PaymentController(
    private val payOrderUseCase: PayOrderUseCase
) {
    @PostMapping("/orders/{orderNumber}/payment")
    fun payOrder(
        @RequestHeader("User-Id") userId: Long,
        @PathVariable orderNumber: String
    ): ResponseEntity<CreatePaymentResponse> {
        val info = payOrderUseCase.payOrder(PayOrderCommand(orderNumber, userId))
        val response = CreatePaymentResponse.of(info)
        return ResponseEntity.ok(response)
    }
}

data class CreatePaymentResponse(
    val payment: PaymentDto
) {
    companion object {
        fun of(info: PaymentInfo): CreatePaymentResponse {
            return CreatePaymentResponse(
                PaymentDto(info.amount.getAmount(), info.createdAt)
            )
        }
    }
}
