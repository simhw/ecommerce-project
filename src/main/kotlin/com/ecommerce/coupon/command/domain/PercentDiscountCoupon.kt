package com.ecommerce.coupon.command.domain

import com.ecommerce.common.model.Money
import com.ecommerce.common.model.Period
import java.math.BigDecimal

/**
 * 비율 할인 쿠폰
 */
class PercentDiscountCoupon(
    id: Long? = null,
    name: String,
    description: String,
    period: Period,
    condition: DiscountCondition,
    val percent: BigDecimal,
    val maxDiscountAmount: Money
) : Coupon(id, name, description, period, condition) {
    override fun getDiscountAmount(amount: Money): Money {
        val discountAmount = amount.multiply(percent)

        if (discountAmount.isLessThan(discountAmount)) {
            return discountAmount
        }
        return maxDiscountAmount
    }
}