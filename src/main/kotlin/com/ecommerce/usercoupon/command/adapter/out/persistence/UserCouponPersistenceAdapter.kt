package com.ecommerce.usercoupon.command.adapter.out.persistence

import com.ecommerce.usercoupon.command.application.out.LoadUserCouponPort
import com.ecommerce.usercoupon.command.application.out.SaveUserCouponPort
import com.ecommerce.usercoupon.command.domain.model.UserCoupon
import org.springframework.stereotype.Repository

@Repository
class UserCouponPersistenceAdapter(
    private val userCouponJpaRepository: UserCouponJapRepository,
    private val userCouponEntityMapper: UserCouponEntityMapper
) : SaveUserCouponPort, LoadUserCouponPort {
    override fun saveUserCoupon(userCoupon: UserCoupon) {
        val userCouponEntity = userCouponEntityMapper.toUserCouponEntity(userCoupon)
        userCouponJpaRepository.save(userCouponEntity)
    }

    override fun loadUserCouponBy(id: Long): UserCoupon {
        val userCouponEntity = userCouponJpaRepository.findById(id)
            .orElseThrow { IllegalArgumentException("not found user coupon") }
        return userCouponEntityMapper.toUserCoupon(userCouponEntity)
    }
}