package com.ecommerce.usercoupon.command.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface UserCouponJapRepository : JpaRepository<UserCouponEntity, Long>