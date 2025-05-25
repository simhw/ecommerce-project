package com.ecommerce.product.command.adapter.`in`.event

import com.ecommerce.order.command.application.`in`.FailOrderUseCase
import com.ecommerce.order.command.domain.OrderPlacedEvent
import com.ecommerce.product.command.application.`in`.DecreaseStockCommand
import com.ecommerce.product.command.application.`in`.DecreaseStockUseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class DecreaseStockWithOrderPlacedEventHandler(
    private val decreaseStockUseCase: DecreaseStockUseCase,
    private val failOrderUseCase: FailOrderUseCase
) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    fun handle(event: OrderPlacedEvent) {
        val order = event.order
        val command = DecreaseStockCommand(
            order.number,
            order.userId,
            order.items.map {
                DecreaseStockCommand.OrderItem(
                    it.productId,
                    it.quantity
                )
            }
        )

        try {
            decreaseStockUseCase.decreaseStock(command)
        } catch (e: Exception) {
            failOrderUseCase.failOrder(command.orderNumber)
        }
    }
}
