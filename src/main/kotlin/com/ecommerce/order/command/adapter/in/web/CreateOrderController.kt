package com.ecommerce.order.command.adapter.`in`.web

import com.ecommerce.common.model.Address
import com.ecommerce.order.command.application.`in`.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/api/v1/orders")
class CreateOrderController(
    val placeOrderUseCase: PlaceOrderUseCase
) {
    @PostMapping
    @RequestMapping("/order")
    fun createOrder(
        @RequestHeader("User-Id") userId: Long,
        @RequestBody request: CreateOrderRequest
    ): ResponseEntity<CreateOrderResponse> {
        val command = request.of(userId)
        val info = placeOrderUseCase.placeOrder(command)
        return ResponseEntity.ok().body(CreateOrderResponse.of(info))
    }
}

data class CreateOrderRequest(
    val address: Address,
    val userCouponId: Long?,
    val items: List<OrderProduct>
) {
    fun of(userId: Long): PlaceOrderCommand {
        return PlaceOrderCommand(
            userId = userId,
            address = address,
            userCouponId = userCouponId,
            items.map { PlaceOrderCommand.OrderItem(it.productId, it.quantity) }
        )
    }
}

data class OrderProduct(
    val productId: Long,
    val quantity: BigDecimal
)

data class CreateOrderResponse(
    val order: OrderDto
) {
    companion object {
        fun of(info: OrderInfo): CreateOrderResponse =
            CreateOrderResponse(
                OrderDto(
                    info.number,
                    info.totalAmounts.getAmount(),
                    info.totalDiscountAmounts.getAmount(),
                    info.createdAt
                )
            )
    }
}
