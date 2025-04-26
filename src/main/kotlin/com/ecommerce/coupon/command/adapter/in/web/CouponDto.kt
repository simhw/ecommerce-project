package com.ecommerce.coupon.command.adapter.`in`.web


data class CouponDto(
    val id: Long,
    val title: String,
    val description: String,
    val type: String,
    val value: String
)