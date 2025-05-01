package com.ecommerce.usercoupon.command.application.out

import com.ecommerce.usercoupon.command.domain.model.UserCoupon

interface LoadUserCouponPort {
    fun loadUserCouponBy(id: Long): UserCoupon
}