package com.ecommerce.coupon.domain.model

import com.ecommerce.common.model.Money

sealed class Coupon {
    abstract fun calculateDiscount(quantity: Long, amount: Money): Money

    class PercentDiscount(
        val discountPercent: Double
    ) : Coupon() {
        override fun calculateDiscount(quantity: Long, amount: Money): Money =
            amount.times(quantity).times(100)
    }

    class AmountDiscount(
        val discountAmount: Money
    ) : Coupon() {
        override fun calculateDiscount(quantity: Long, amount: Money) =
            amount.times(quantity).minus(discountAmount)
    }
}