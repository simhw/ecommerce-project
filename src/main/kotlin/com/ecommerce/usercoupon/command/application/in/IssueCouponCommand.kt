package com.ecommerce.usercoupon.command.application.`in`

data class IssueCouponCommand(
    val userId: Long,
    val couponId: Long
)