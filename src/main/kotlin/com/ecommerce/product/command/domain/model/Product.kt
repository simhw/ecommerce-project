package com.ecommerce.product.command.domain.model

import com.ecommerce.common.model.Money
import java.math.BigDecimal

class Product(
    val id: Long? = null,
    var name: String,
    var description: String,
    var price: Money,
    var stock: ProductStock,
    var status: ProductStatus,
) {

    /**
     * 상품 재고 감소
     * 1-1. ✅ 상품 상태 및 재고 확인
     * 1-2. 재고가 있는 경우 재고 차감
     * TODO: (event) - before commit
     */
    fun reserve(quantity: BigDecimal) {
        verifyAvailableForSale()
        verifyEnoughStock(quantity)
        this.stock.value = this.stock.value.minus(quantity)
    }

    fun cancel(quantity: BigDecimal) {
        this.stock.value = this.stock.value.plus(quantity)
    }

    private fun verifyAvailableForSale() {
        if (status != ProductStatus.SELL) {
            throw IllegalArgumentException("is not available for sale")
        }
    }

    private fun verifyEnoughStock(count: BigDecimal) {
        if (this.stock.value < count) {
            throw IllegalArgumentException("not enough stock")
        }
    }
}