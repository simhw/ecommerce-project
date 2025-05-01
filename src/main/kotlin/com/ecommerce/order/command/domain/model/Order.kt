package com.ecommerce.order.command.domain.model

import com.ecommerce.common.model.Address
import com.ecommerce.common.model.Money
import com.ecommerce.user.domain.model.User
import com.ecommerce.usercoupon.command.domain.model.UserCoupon
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Order(
    val id: Long?,
    var number: String?,
    val orderer: User,
    var address: Address,
    val items: List<OrderLineItem>,
    var totalAmounts: Money = Money.ZERO,
    var totalDiscountAmounts: Money = Money.ZERO,
    var status: OrderStatus = OrderStatus.PENDING,
    val createdAt: LocalDateTime
) {
    companion object {
        fun of(orderer: User, address: Address, items: List<OrderLineItem>): Order {
            return Order(
                id = null,
                number = null,
                orderer = orderer,
                address = address,
                items = items,
                createdAt = LocalDateTime.now()
            )
        }
    }

    fun place(userCoupon: UserCoupon?) {
        generateOrderNumber()
        calculateTotalAmount()
        userCoupon?.let { applyCoupon(it) }
        adjustProductStock()
        ordered()
    }

    /**
     * 주문 번호 생성
     */
    private fun generateOrderNumber() {
        val now = createdAt.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        this.number = "ORD$now-${(1000..9999).random()}"
    }

    /**
     * 총 주문 금액 계산
     *
     */
    private fun calculateTotalAmount() {
        this.totalAmounts = Money.sum(items) { it.calculateAmount() }
    }

    /**
     * 상품 재고 감소
     * 1-1. ✅상품의 재고가 주문 수량보다 많은지 확인한다.
     * 1-2. 재고가 있는 경우 재고를 차감한다.(event) - before commit
     */
    private fun adjustProductStock() {
        items.forEach {
            it.product.verifyEnoughStock(it.quantity)
            it.product.adjustStock(-it.quantity)
        }
    }

    /**
     * 쿠폰 적용 및 할인 금액 계산
     * 1-1. ✅할인 쿠폰 사용 기간을 확인한다.
     * 1-2. 할인 쿠폰 사용 시 할인 금액을 계산한다.
     * 1-3. 할인 쿠폰을 사용 처리한다.(event) - after commit
     */
    private fun applyCoupon(userCoupon: UserCoupon) {
        userCoupon.coupon.verifyPeriodOfUse()
        this.totalDiscountAmounts = userCoupon.coupon.calculateDiscountAmount(totalAmounts)
        userCoupon.used()
    }

    /**
     * order → product
     * 상품 재고 원복
     */
    private fun restoreProductStock() {
        items.forEach {
            it.product.adjustStock(it.quantity)
        }
    }

    private fun ordered() {
        this.status = OrderStatus.ORDERED
    }

    fun paid() {
        this.status = OrderStatus.PAID
    }

    fun canceled() {
        this.status = OrderStatus.CANCELED
    }

}

