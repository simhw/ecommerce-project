package com.ecommerce.coupon.adapter.out.persistence

import com.ecommerce.coupon.adapter.out.persistence.entity.CouponEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CouponJpaRepository : JpaRepository<CouponEntity, Long> {
}