package com.ecommerce.product.command.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

class Stock(
    val id: Long? = null,
    var value: BigDecimal,
    val productId: Long,
    val updatedAt: LocalDateTime
) {
    fun increase(quantity: BigDecimal) {
        value = value.plus(quantity)
    }

    /**
     * 상품 재고 감소
     * 1-1. ✅ 재고 수량 확인
     * 1-2. 재고가 있는 경우 재고 차감
     */
    fun decrease(quantity: BigDecimal) {
        if (!validateEnoughStock(quantity)) {
            throw IllegalArgumentException("not enough stock: $value")
        }
        value = value.minus(quantity)
    }

    private fun validateEnoughStock(quantity: BigDecimal): Boolean {
        return value >= quantity
    }
}