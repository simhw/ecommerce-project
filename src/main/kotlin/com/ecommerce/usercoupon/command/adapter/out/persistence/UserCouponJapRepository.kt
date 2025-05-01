package com.ecommerce.usercoupon.command.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface UserCouponJapRepository : JpaRepository<UserCouponEntity, Long> {
    @Query("select uc from UserCouponEntity uc join fetch uc.coupon where uc.id = :id")
    override fun findById(id: Long): Optional<UserCouponEntity>
}