package com.ecommerce.usercoupon.command.domain.service

import com.ecommerce.coupon.application.LoadCouponPort
import com.ecommerce.user.application.out.LoadUserPort
import com.ecommerce.usercoupon.command.application.`in`.IssueCouponCommand
import com.ecommerce.usercoupon.command.application.`in`.IssueCouponUseCase
import com.ecommerce.usercoupon.command.application.out.SaveUserCouponPort
import com.ecommerce.usercoupon.command.domain.model.UserCoupon
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class IssueCouponService(
    private val loadUserPort: LoadUserPort,
    private val loadCouponPort: LoadCouponPort,
    private val saveUserCouponPort: SaveUserCouponPort
) : IssueCouponUseCase {
    @Transactional
    override fun issueCoupon(command: IssueCouponCommand) {
        val user = loadUserPort.loadUserBy(command.userId)
        val coupon = loadCouponPort.loadCouponBy(command.couponId)
        val userCoupon = UserCoupon.issue(user, coupon)
        saveUserCouponPort.saveUserCoupon(userCoupon)
    }
}