package com.ecommerce.coupon.command.adapter.out.persistence

import com.ecommerce.coupon.command.adapter.out.persistence.entity.CouponEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CouponJpaRepository : JpaRepository<CouponEntity, Long> {
}