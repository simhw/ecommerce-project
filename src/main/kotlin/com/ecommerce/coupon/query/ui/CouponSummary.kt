package com.ecommerce.coupon.query.ui

import java.math.BigDecimal

data class CouponSummary(
    val id: Long,
    val name: String,
    val description: String,
    val discountType: String,
    val percent: BigDecimal?,
    val discountAmount: BigDecimal?,
)