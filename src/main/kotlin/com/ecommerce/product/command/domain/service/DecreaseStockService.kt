package com.ecommerce.product.command.domain.service

import com.ecommerce.common.support.DistributeLock
import com.ecommerce.product.command.application.`in`.DecreaseStockCommand
import com.ecommerce.product.command.application.`in`.DecreaseStockUseCase
import com.ecommerce.product.command.application.out.StockPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager

@Service
class DecreaseStockService(
    private val distributeLock: DistributeLock,
    private val decreaseStockTxExecutor: DecreaseStockTransactionalExecutor
) : DecreaseStockUseCase {
    override fun decreaseStock(command: DecreaseStockCommand) {
        val keys = command.orderItems
            .map { it.productId.toString() }
            .toTypedArray()
        distributeLock.multiLock(
            keys = keys,
            waitTime = 10,
            leaseTime = 15
        ) {
            decreaseStockTxExecutor.execute(command)
        }
    }
}

@Service
class DecreaseStockTransactionalExecutor(
    private val stockPort: StockPort
) {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun execute(command: DecreaseStockCommand) {
        println("Start DecreaseStockTransactionalExecutor txName: ${TransactionSynchronizationManager.getCurrentTransactionName()}")
        val stocks = command.orderItems
            .map { stockPort.loadStockByProductId(it.productId) }
            .associateBy { it.productId }
        command.orderItems.map {
            val stock = stocks[it.productId]
                ?: throw IllegalArgumentException("not found stock")
            println("decrease stock user.id = ${command.userId}, product.id = ${it.productId}, stock = ${stock.value}")
            stock.decrease(it.quantity)
        }
        stockPort.saveAllStock(stocks.values.toList())
    }

    @Transactional
    fun decreaseStockWithPessimisticLock(command: DecreaseStockCommand) {
        val stocks = command.orderItems
            .map { stockPort.loadStockForUpdateByProductId(it.productId) }
            .associateBy { it.productId }
        command.orderItems.forEach {
            val stock = stocks[it.productId]
                ?: throw IllegalArgumentException("not found stock")
            stock.decrease(it.quantity)
        }
        stockPort.saveAllStock(stocks.values.toList())
    }
}