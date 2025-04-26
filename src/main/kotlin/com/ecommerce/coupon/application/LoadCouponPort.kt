package com.ecommerce.coupon.application

import com.ecommerce.coupon.domain.model.Coupon

interface LoadCouponPort {
    fun loadCouponBy(id: Long): Coupon
}
