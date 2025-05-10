package com.ecommerce.usercoupon.command.domain.model

import com.ecommerce.common.model.Money
import com.ecommerce.coupon.command.domain.model.Coupon
import java.time.LocalDateTime

class UserCoupon(
    val id: Long? = null,
    val userId: Long,
    val coupon: Coupon,
    var status: UserCouponStatus,
    val createdAt: LocalDateTime? = null
) {
    companion object {
        fun issue(userId: Long, coupon: Coupon): UserCoupon {
            coupon.verifyPeriodOfIssue()
            return UserCoupon(userId = userId, coupon = coupon, status = UserCouponStatus.UNUSED)
        }
    }

    /**
     * 쿠폰 사용
     */
    fun apply(amount: Money): Money {
        used()
        return coupon.calculateDiscountAmount(amount)
    }

    private fun used() {
        verifyUsableStatus()
        this.status = UserCouponStatus.USED
    }

    private fun expired() {
        this.status = UserCouponStatus.EXPIRED
    }

    private fun verifyUsableStatus() {
        if (status != UserCouponStatus.UNUSED) {
            throw IllegalStateException("can not use coupon: $status")
        }
    }
}