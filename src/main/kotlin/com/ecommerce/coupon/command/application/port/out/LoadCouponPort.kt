package com.ecommerce.coupon.command.application.port.out

import com.ecommerce.coupon.domain.model.Coupon

interface LoadCouponPort {
    fun loadCouponBy(id: Long): Coupon
}
