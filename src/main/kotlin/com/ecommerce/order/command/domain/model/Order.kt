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
    val userId: Long,
    var address: Address,
    val items: List<OrderLineItem>?,
    var totalAmounts: Money = Money.ZERO,
    var totalDiscountAmounts: Money = Money.ZERO,
    var status: OrderStatus = OrderStatus.PENDING,
    val createdAt: LocalDateTime
) {
    companion object {
        fun of(user: User, address: Address, items: List<OrderLineItem>): Order {
            return Order(
                id = null,
                number = null,
                userId = user.getIdOrThrow(),
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
     */
    private fun calculateTotalAmount() {
        this.totalAmounts = Money.sum(items) { it.amount }
    }

    /**
     * 상품 재고 감소
     */
    private fun adjustProductStock() = items?.map { it.product.adjustStockForSale(it.quantity) }

    /**
     * 상품 재고 원복
     */
    private fun restoreProductStock() = items?.map { it.product.adjustStockForCancel(it.quantity) }

    /**
     * 쿠폰 적용 및 할인 금액 계산
     * 1-1. ✅할인 쿠폰 사용 기간을 확인한다.
     * 1-2. 할인 쿠폰 사용 시 할인 금액을 계산한다.
     * 1-3. 할인 쿠폰을 사용 처리한다.(event) - after commit
     */
    private fun applyCoupon(userCoupon: UserCoupon) {
        val coupon = userCoupon.coupon
        coupon.verifyPeriodOfUse()
        coupon.isSatisfyCondition(totalAmounts)
        this.totalDiscountAmounts = coupon.calculateDiscountAmount(totalAmounts)
        userCoupon.used()
    }

    private fun ordered() {
        this.status = OrderStatus.ORDERED
    }

    fun paid() {
        if (this.status != OrderStatus.ORDERED) {
            throw IllegalArgumentException("is not valid order")
        }
        this.status = OrderStatus.PAID
    }

    fun canceled() {
        if (this.status == OrderStatus.PAID) {
            throw IllegalArgumentException("already paid order")
        }
        this.status = OrderStatus.CANCELED
    }
}

