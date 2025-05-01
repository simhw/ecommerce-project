package com.ecommerce.order.command.domain.model

enum class OrderStatus(
    description: String
) {
    PENDING("주문대기"),
    ORDERED("주문완료"),
    PAID("결제완료"),
    CANCELED("주문취소")
}