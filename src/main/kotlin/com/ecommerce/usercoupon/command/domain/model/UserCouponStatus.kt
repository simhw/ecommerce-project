package com.ecommerce.usercoupon.command.domain.model

enum class UserCouponStatus(
    description: String
) {
    UNUSED("사용가능"),
    USED("사용완료"),
    EXPIRED("만료"),
    CANCELLED("취소")
}
