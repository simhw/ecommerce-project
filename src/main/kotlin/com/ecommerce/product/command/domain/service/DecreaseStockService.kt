package com.ecommerce.product.command.domain.service

import com.ecommerce.product.command.application.`in`.DecreaseStockCommand
import com.ecommerce.product.command.application.`in`.DecreaseStockUseCase
import com.ecommerce.product.command.application.out.StockPort
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class DecreaseStockService(
    private val stockPort: StockPort
) : DecreaseStockUseCase {
    @Transactional
    override fun decreaseStock(command: DecreaseStockCommand) {
        val stocks = command.orderItems
            .map { stockPort.loadStockForUpdateByProductId(it.productId) }
            .associateBy { it.productId }

        command.orderItems.map {
            val stock = stocks[it.productId] ?: throw IllegalArgumentException("not found stock")
            stock.decrease(it.quantity)
        }
        stockPort.saveAllStock(stocks.values.toList())
    }
}