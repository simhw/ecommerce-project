package com.ecommerce.usercoupon.command.domain.model

import com.ecommerce.coupon.domain.model.Coupon
import com.ecommerce.user.domain.model.User
import java.time.LocalDateTime

class UserCoupon(
    val id: Long? = null,
    val user: User,
    val coupon: Coupon,
    var used: Boolean,
    val createdAt: LocalDateTime? = null
) {
    companion object {
        fun issue(user: User, coupon: Coupon): UserCoupon {
            user.verifyActiveUser()
            coupon.verifyPeriodOfIssue()
            return UserCoupon(user = user, coupon = coupon, used = false)
        }
    }
}