package com.ecommerce.coupon.command.adapter.out.persistence

import com.ecommerce.coupon.command.application.port.out.LoadCouponPort
import com.ecommerce.coupon.command.domain.Coupon
import org.springframework.stereotype.Repository

@Repository
class CouponPersistenceAdapter(
    private val repository: CouponJpaRepository,
    private val mapper: CouponEntityMapper
) : LoadCouponPort {
    override fun loadCouponBy(id: Long): Coupon {
        val coupon = repository.findById(id)
            .orElseThrow { IllegalArgumentException("Not found coupon: ${id}") }

        return mapper.to(coupon)
    }
}