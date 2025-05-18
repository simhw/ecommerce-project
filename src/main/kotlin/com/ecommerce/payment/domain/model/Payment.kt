package com.ecommerce.payment.domain.model

import com.ecommerce.account.domain.model.Account
import com.ecommerce.common.model.Money
import com.ecommerce.order.command.domain.model.Order
import com.ecommerce.user.domain.model.User
import java.time.LocalDateTime

class Payment(
    val id: Long? = null,
    var amount: Money,
    val order: Order,
    val user: User,
    val createdAt: LocalDateTime
) {
    companion object {
        fun of(order: Order, user: User): Payment = Payment(
            amount = Money.ZERO,
            order = order,
            user = user,
            createdAt = LocalDateTime.now(),
        )
    }

    /**
     * 결제하기
     */
    fun pay(account: Account) {
        order.pay()
        this.amount = calculatePaymentAmount()
        account.withdraw(this.amount)
    }

    /**
     * 결제 금액 계산
     */
    private fun calculatePaymentAmount(): Money {
        return order.totalAmounts.minus(order.totalDiscountAmounts)
    }
}