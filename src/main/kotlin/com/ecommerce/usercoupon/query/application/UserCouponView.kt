package com.ecommerce.usercoupon.query.application

import com.ecommerce.coupon.query.ui.CouponSummary
import java.time.LocalDateTime

data class UserCouponView(
    val coupon: CouponSummary,
    val createdAt: LocalDateTime,
)