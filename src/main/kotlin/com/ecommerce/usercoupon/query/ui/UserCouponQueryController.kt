package com.ecommerce.usercoupon.query.ui

import com.ecommerce.usercoupon.query.application.UserCouponQueryService
import com.ecommerce.usercoupon.query.application.UserCouponView
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class UserCouponQueryController(
    private val userCouponQueryService: UserCouponQueryService
) {
    @GetMapping("/users/{userId}/coupons")
    fun searchUserCoupons(
        @PathVariable userId: Long,
    ): ResponseEntity<GetUserCouponResponse> {
        val result = userCouponQueryService.searchUserCouponsBy(userId)
        val response = GetUserCouponResponse(userCoupons = result)
        return ResponseEntity.ok().body(response)
    }
}

data class GetUserCouponResponse(
    val userCoupons: List<UserCouponView>?
)
