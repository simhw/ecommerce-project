package com.ecommerce.coupon.command.domain

import com.ecommerce.common.model.Money
import com.ecommerce.common.model.Period
import java.time.LocalDateTime

sealed class Coupon(
    val id: Long? = null,
    val name: String,
    val description: String,
    val period: Period,
    /**
     * 사용(할인) 조건
     */
    val condition: DiscountCondition
) {
    fun calculateDiscount(amount: Money): Money {
        if (condition.isSatisfiedBy(amount)) {
            return getDiscountAmount(amount)
        }
        return Money.ZERO
    }

    fun verifyAvailable() {
        if (!isAvailable()) {
            throw IllegalArgumentException("Not available date")
        }
    }

    private fun isAvailable(): Boolean {
        return period.isWithin(LocalDateTime.now())
    }

    abstract fun getDiscountAmount(amount: Money): Money
}
