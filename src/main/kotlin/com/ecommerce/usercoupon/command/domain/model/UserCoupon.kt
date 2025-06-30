package com.ecommerce.usercoupon.command.domain.model

import com.ecommerce.common.model.Money
import com.ecommerce.coupon.command.domain.Coupon
import java.time.LocalDateTime

class UserCoupon(
    val userId: Long,
    val coupon: Coupon,
    var status: UserCouponStatus,
    val id: Long? = null,
    val createdAt: LocalDateTime? = null
) {
    companion object {
        fun issue(userId: Long, coupon: Coupon): UserCoupon {
            coupon.verifyAvailable()
            return UserCoupon(userId, coupon, UserCouponStatus.UNUSED)
        }
    }

    /**
     * 쿠폰 적용
     */
    fun apply(amount: Money): Money {
        used()
        return coupon.calculateDiscount(amount)
    }

    private fun used() {
        if (!isAvailable()) {
            throw IllegalArgumentException("User coupons unavailable status")
        }

        this.status = UserCouponStatus.USED
    }

    private fun isAvailable(): Boolean {
        return status == UserCouponStatus.UNUSED
    }

    private fun expired() {
        this.status = UserCouponStatus.EXPIRED
    }
}