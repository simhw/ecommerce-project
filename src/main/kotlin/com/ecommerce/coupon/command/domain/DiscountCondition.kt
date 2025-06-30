package com.ecommerce.coupon.command.domain

import com.ecommerce.common.model.Money
import com.ecommerce.common.model.Period
import java.time.LocalDateTime

/**
 * 쿠폰 할인 조건
 */
sealed interface DiscountCondition {
    fun isSatisfiedBy(amount: Money): Boolean
}

/**
 * 기간 조건
 * 설정 기간 내 사용 가능
 */
class PeriodCondition(
    val period: Period
) : DiscountCondition {
    override fun isSatisfiedBy(amount: Money): Boolean {
        return period.isWithin(LocalDateTime.now())
    }
}

/**
 * 금액 조건
 * 주문 금액이 기준 금액보다 커야 사용 가능
 */
class AmountCondition(
    val amount: Money
) : DiscountCondition {
    override fun isSatisfiedBy(amount: Money): Boolean {
        return amount.isGreaterThanOrEqual(this.amount)
    }
}

/**
 * 무조건
 */
class NoneCondition(
) : DiscountCondition {
    override fun isSatisfiedBy(amount: Money): Boolean {
        return true
    }
}

/**
 * and 조건
 * 여러 조건 중 모두 충족 시 할인
 */
class AllCondition(
    val conditions: List<DiscountCondition>
) : DiscountCondition {
    override fun isSatisfiedBy(amount: Money): Boolean {
        return conditions.all {
            it.isSatisfiedBy(amount)
        }
    }
}

/**
 * or 조건
 * 여러 조건 중 하나라도 충족 시 할인
 */
class AnyCondition(
    val conditions: List<DiscountCondition>
) : DiscountCondition {
    override fun isSatisfiedBy(amount: Money): Boolean {
        return conditions.any {
            it.isSatisfiedBy(amount)
        }
    }
}
