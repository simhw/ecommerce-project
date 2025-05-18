package com.ecommerce.product.command.domain.service

import com.ecommerce.common.support.DistributeLock
import com.ecommerce.product.command.application.`in`.DecreaseStockCommand
import com.ecommerce.product.command.application.out.StockPort
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
class IncreaseStockService(
    private val stockPort: StockPort,
    private val distributeLock: DistributeLock,
    private val transactionTemplate: TransactionTemplate
) {
    fun increaseStock(command: DecreaseStockCommand) {
        val productIds = command.orderItems
            .map { it.productId.toString() }
            .toTypedArray()

        distributeLock.multiLock(*productIds) {
            transactionTemplate.execute {
                val stocks = command.orderItems
                    .map { stockPort.loadStockByProductId(it.productId) }
                    .associateBy { it.productId }
                command.orderItems.forEach {
                    val stock = stocks[it.productId]
                        ?: throw IllegalArgumentException("not found stock")
                    stock.increase(it.quantity)
                }
                stockPort.saveAllStock(stocks.values.toList())
            }
        }
    }
}