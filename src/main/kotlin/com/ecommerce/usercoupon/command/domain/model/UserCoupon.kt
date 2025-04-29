package com.ecommerce.usercoupon.command.domain.model

import com.ecommerce.coupon.domain.model.Coupon
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
            return UserCoupon(
                userId = userId,
                coupon = coupon,
                status = UserCouponStatus.UNUSED
            )
        }
    }

    fun used() {
        if (status != UserCouponStatus.UNUSED) {
            throw IllegalStateException("can not use coupon: $status")
        }
        this.status = UserCouponStatus.USED
    }

    fun expired() {
        this.status = UserCouponStatus.EXPIRED
    }
}