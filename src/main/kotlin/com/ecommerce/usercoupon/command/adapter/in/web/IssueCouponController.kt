package com.ecommerce.usercoupon.command.adapter.`in`.web

import com.ecommerce.usercoupon.command.application.`in`.IssueCouponCommand
import com.ecommerce.usercoupon.command.application.`in`.IssueCouponUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class IssueCouponController(
    private val issueCouponUseCase: IssueCouponUseCase
) {
    @PostMapping("/coupons/{couponId}/issue")
    fun issueCoupon(
        @RequestHeader("User-Id") userId: Long,
        @PathVariable couponId: Long,
    ): ResponseEntity<String> {
        issueCouponUseCase.issueCoupon(IssueCouponCommand(userId, couponId))
        return ResponseEntity<String>(HttpStatus.CREATED)
    }
}