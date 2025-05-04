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

    /**
     * 상품 재고 감소
     * 1-1. ✅상품의 재고가 주문 수량보다 많은지 확인한다.
     * 1-2. TODO: 재고가 있는 경우 재고를 차감한다.(event) - before commit
     */
    fun adjustStockForSale(count: BigDecimal) {
        verifyAvailableForSale()
        verifyEnoughStock(count)
        this.stock.value = this.stock.value.minus(count)
    }

    fun adjustStockForCancel(count: BigDecimal) {
        verifyAvailableForSale()
        this.stock.value = this.stock.value.plus(count)
    }
}