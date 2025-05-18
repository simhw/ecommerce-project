package com.ecommerce.usercoupon.command.application.out

import com.ecommerce.usercoupon.command.domain.model.UserCoupon

interface UserCouponPort {
    fun loadUserCouponById(id: Long): UserCoupon

    fun saveUserCoupon(userCoupon: UserCoupon)
}