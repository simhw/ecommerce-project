package com.ecommerce.coupon.command.domain

import com.ecommerce.common.model.Money
import com.ecommerce.common.model.Period

/**
 * 금액 할인 쿠폰
 */
class AmountDiscountCoupon(
    id: Long? = null,
    name: String,
    description: String,
    period: Period,
    condition: DiscountCondition,
    val amount: Money
) : Coupon(id, name, description, period, condition) {
    override fun getDiscountAmount(amount: Money): Money {
        return this.amount
    }
}

