package com.ecommerce.usercoupon.command.application.out

import com.ecommerce.usercoupon.command.domain.model.UserCoupon

interface SaveUserCouponPort {
    fun saveUserCoupon(coupon: UserCoupon)
}