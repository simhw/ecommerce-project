package com.ecommerce.coupon.command.domain.model

import com.ecommerce.common.model.Money
import com.ecommerce.common.model.DateTimePeriod
import java.math.BigDecimal

import java.time.LocalDateTime

abstract class Coupon(
    val id: Long? = null,
    var name: String,
    var description: String,
    private var minOrderAmount: Money,
    var maxDiscountAmount: Money,
    private var useOfPeriod: DateTimePeriod,
    private var issueOfPeriod: DateTimePeriod,
) {
    abstract fun calculateDiscountAmount(amount: Money): Money

    fun isSatisfyCondition(amount: Money): Boolean {
        verifyPeriodOfUse()
        verifyMinOrderAmount(amount)
        return true;
    }

    private fun verifyPeriodOfUse() {
        if (!useOfPeriod.isWithin(LocalDateTime.now())) {
            throw IllegalArgumentException("not valid period of use")
        }
    }

    private fun verifyMinOrderAmount(amount: Money) {
        if (minOrderAmount.amount > amount.amount) {
            throw IllegalArgumentException("invalid amount")
        }
    }

    fun verifyPeriodOfIssue() {
        if (!issueOfPeriod.isWithin(LocalDateTime.now())) {
            throw IllegalArgumentException("not valid period of issue")
        }
    }
}

class PercentDiscountCoupon(
    id: Long? = null,
    name: String,
    description: String,
    minOrderAmount: Money,
    maxDiscountAmount: Money,
    useOfPeriod: DateTimePeriod,
    issueOfPeriod: DateTimePeriod,
    private val percent: BigDecimal
) : Coupon(id, name, description, minOrderAmount, maxDiscountAmount, useOfPeriod, issueOfPeriod) {
    override fun calculateDiscountAmount(amount: Money): Money {
        if (!isSatisfyCondition(amount)) {
            return Money.ZERO
        }
        val discountedAmount = amount.multiply(this.percent)
        return if (discountedAmount.isLessThan(maxDiscountAmount))
            discountedAmount
        else
            maxDiscountAmount
    }
}

class AmountDiscountCoupon(
    id: Long? = null,
    name: String,
    description: String,
    minOrderAmount: Money,
    maxDiscountAmount: Money,
    useOfPeriod: DateTimePeriod,
    issueOfPeriod: DateTimePeriod,
    private val amount: Money
) : Coupon(id, name, description, minOrderAmount, maxDiscountAmount, useOfPeriod, issueOfPeriod) {
    override fun calculateDiscountAmount(amount: Money): Money {
        if (!isSatisfyCondition(amount)) {
            return Money.ZERO
        }
        return this.amount
    }
}