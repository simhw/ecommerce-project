package com.ecommerce.usercoupon.command.adapter.out.persistence

import com.ecommerce.usercoupon.command.application.out.SaveUserCouponPort
import com.ecommerce.usercoupon.command.domain.model.UserCoupon
import org.springframework.stereotype.Repository

@Repository
class UserCouponPersistenceAdapter(
    private val userCouponJpaRepository: UserCouponJapRepository,
    private val userCouponEntityMapper: UserCouponEntityMapper
) : SaveUserCouponPort {
    override fun saveUserCoupon(coupon: UserCoupon) {
        val userCouponEntity = userCouponEntityMapper.toUserCouponEntity(coupon)
        userCouponJpaRepository.save(userCouponEntity)
    }
}