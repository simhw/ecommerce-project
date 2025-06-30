package com.ecommerce.order.command.domain.model

import com.ecommerce.common.model.Address
import com.ecommerce.common.model.Money
import com.ecommerce.common.util.NumberUtils
import com.ecommerce.usercoupon.command.domain.model.UserCoupon
import java.time.LocalDateTime

class Order(
    val id: Long? = null,
    val number: String,
    val userId: Long,
    var address: Address,
    val items: List<OrderLineItem>,
    var totalAmounts: Money = Money.ZERO,
    var totalDiscountAmounts: Money = Money.ZERO,
    var status: OrderStatus = OrderStatus.PENDING,
    val createdAt: LocalDateTime
) {
    companion object {
        fun of(userId: Long, address: Address, items: List<OrderLineItem>): Order {
            return Order(
                number = NumberUtils.generateOrderNumber(),
                userId = userId,
                address = address,
                items = items,
                createdAt = LocalDateTime.now()
            )
        }
    }

    /**
     * 주문하기
     */
    fun place(userCoupon: UserCoupon?) {
        this.totalAmounts = calculateTotalAmount()
        userCoupon?.let { applyCoupon(userCoupon) }
        ordered()
    }

    fun fail() {
        if (!isNotYetPayed()) {
            throw IllegalArgumentException("already paid order $number")
        }
        failed()
    }

    fun cancel() {
        if (!isNotYetShipped()) {
            throw IllegalArgumentException("can not cancel order")
        }
        canceled()
    }

    fun pay() {
        if (!isNotYetShipped()) {
            throw IllegalArgumentException("can not cancel order")
        }
        paid()
    }

    /**
     * 총 주문 금액 계산
     */
    private fun calculateTotalAmount(): Money {
        return Money.sum(items) { it.amount }
    }

    private fun applyCoupon(userCoupon: UserCoupon) {
        this.totalDiscountAmounts = userCoupon.apply(totalAmounts)
    }

    private fun ordered() {
        this.status = OrderStatus.ORDERED
    }

    private fun canceled() {
        this.status = OrderStatus.CANCELED
    }

    private fun failed() {
        this.status = OrderStatus.FAILED
    }

    private fun paid() {
        this.status = OrderStatus.PAID
    }

    private fun isNotYetPayed(): Boolean {
        return status === OrderStatus.ORDERED
    }

    private fun isNotYetShipped(): Boolean {
        return status === OrderStatus.ORDERED || status === OrderStatus.PAID
    }
}

