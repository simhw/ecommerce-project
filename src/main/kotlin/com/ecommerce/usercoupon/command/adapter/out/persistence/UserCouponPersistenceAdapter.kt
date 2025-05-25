package com.ecommerce.usercoupon.command.adapter.out.persistence

import com.ecommerce.usercoupon.command.application.out.UserCouponPort
import com.ecommerce.usercoupon.command.domain.model.UserCoupon
import org.springframework.stereotype.Repository

@Repository
class UserCouponPersistenceAdapter(
    private val userCouponJpaRepository: UserCouponJapRepository,
    private val userCouponEntityMapper: UserCouponEntityMapper
) : UserCouponPort {
    override fun loadUserCouponById(id: Long): UserCoupon {
        val userCouponEntity = userCouponJpaRepository.findById(id)
            .orElseThrow { IllegalArgumentException("not found user coupon") }
        return userCouponEntityMapper.toUserCoupon(userCouponEntity)
    }

    override fun saveUserCoupon(userCoupon: UserCoupon) {
        val userCouponEntity = userCouponEntityMapper.toUserCouponEntity(userCoupon)
        userCouponJpaRepository.save(userCouponEntity)
    }
}