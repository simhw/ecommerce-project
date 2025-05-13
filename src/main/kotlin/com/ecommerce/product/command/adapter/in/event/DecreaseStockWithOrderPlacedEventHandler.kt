package com.ecommerce.product.command.adapter.`in`.event

import com.ecommerce.order.command.domain.OrderPlacedEvent
import com.ecommerce.product.command.application.`in`.DecreaseStockCommand
import com.ecommerce.product.command.application.`in`.DecreaseStockUseCase
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class DecreaseStockWithOrderPlacedEventHandler(
    private val decreaseStockUseCase: DecreaseStockUseCase
) {
    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handle(orderPlacedEvent: OrderPlacedEvent) {
        val order = orderPlacedEvent.order
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
        decreaseStockUseCase.decreaseStock(command)
    }
}
