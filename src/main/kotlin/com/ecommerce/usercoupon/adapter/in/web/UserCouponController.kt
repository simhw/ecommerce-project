package com.ecommerce.usercoupon.adapter.`in`.web

import com.ecommerce.coupon.adapter.`in`.web.CouponDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1")
class UserCouponController {
    @PostMapping("/coupons/{couponId}/issue")
    fun issueCoupon(
        @PathVariable couponId: Long,
    ): UserCouponDto = UserCouponDto(
        1L,
        CouponDto(
            1L,
            "10% 쿠폰",
            "쿠폰 설명",
            "AMOUNT_DISCOUNT",
            "1000"
        )
    )

    @GetMapping("/users/{userId}/coupons")
    fun findUserCoupons(
        @PathVariable userId: Long,
    ): UserCouponDto = UserCouponDto(
        1L,
        CouponDto(
            1L,
            "10% 쿠폰",
            "쿠폰 설명",
            "AMOUNT_DISCOUNT",
            "1000"
        )
    )
}