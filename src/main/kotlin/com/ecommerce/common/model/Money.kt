package com.ecommerce.common.model

import java.math.BigDecimal

data class Money(
    val amount: BigDecimal
) {
    companion object {
        fun of(amount: BigDecimal) = Money(amount)
    }

    fun plus(money: Money): Money {
        if (money.amount <= BigDecimal.ZERO) {
            throw ArithmeticException()
        }
        return Money(this.amount.add(money.amount))
    }

    fun minus(money: Money): Money {
        if (this.amount < money.amount) {
            throw ArithmeticException()
        }
        return Money(amount - money.amount)
    }

    fun times(money: Long): Money {
        return Money(amount * BigDecimal.valueOf(money.toDouble()))
    }
}