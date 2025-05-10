package com.ecommerce.coupon.command.adapter.out.persistence

import com.ecommerce.coupon.command.application.port.out.LoadCouponPort
import com.ecommerce.coupon.command.domain.model.Coupon
import org.springframework.stereotype.Repository

@Repository
class CouponPersistenceAdapter(
    private val couponJpaRepository: CouponJpaRepository,
    private val couponEntityMapper: CouponEntityMapper
) : LoadCouponPort {
    override fun loadCouponBy(id: Long): Coupon {
        val couponEntity = couponJpaRepository.findById(id)
            .orElseThrow { IllegalArgumentException("not found coupon") }
        return couponEntityMapper.toCoupon(couponEntity)
    }
}