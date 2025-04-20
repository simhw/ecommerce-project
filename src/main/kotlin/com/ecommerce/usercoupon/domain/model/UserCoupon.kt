package com.ecommerce.usercoupon.domain.model

import com.ecommerce.coupon.domain.model.Coupon
import org.apache.catalina.User
import java.time.LocalDateTime

class UserCoupon(
    val id: Long,
    val user: User,
    val coupon: Coupon,
    val used: Boolean = false,
    val createdAt: LocalDateTime
) {
}