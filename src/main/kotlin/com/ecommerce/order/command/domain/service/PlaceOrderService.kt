package com.ecommerce.order.command.domain.service

import com.ecommerce.order.command.application.out.SaveOrderPort
import com.ecommerce.order.command.application.`in`.OrderInfo
import com.ecommerce.order.command.application.`in`.PlaceOrderUseCase
import com.ecommerce.order.command.application.`in`.PlaceOrderCommand
import com.ecommerce.order.command.domain.OrderPlacedEvent
import com.ecommerce.order.command.domain.model.Order
import com.ecommerce.order.command.domain.model.OrderLineItem
import com.ecommerce.product.command.application.out.ProductPort

import com.ecommerce.user.application.out.LoadUserPort
import com.ecommerce.usercoupon.command.application.out.UserCouponPort
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager

@Service
class PlaceOrderService(
    private val loadUserPort: LoadUserPort,
    private val userCouponPort: UserCouponPort,
    private val productPort: ProductPort,
    private val saveOrderPort: SaveOrderPort,
    private val eventPublisher: ApplicationEventPublisher
) : PlaceOrderUseCase {
    @Transactional
    override fun placeOrder(command: PlaceOrderCommand): OrderInfo {
        println("Start PlaceOrderUseCase txName: ${TransactionSynchronizationManager.getCurrentTransactionName()}")
        // 유효 회원 검증
        val user = loadUserPort.loadUserBy(command.userId)
            .also { it.verifyActiveUser() }
        // 상품 판매 상태 검증
        val products = command.items
            .map { productPort.loadProductBy(it.productId) }
            .onEach { it.verifyAvailableForSale() }
            .associateBy { it.getIdOrThrow() }
        val userCoupon = command.userCouponId?.let { userCouponPort.loadUserCouponById(it) }

        val order = Order.of(
            user.getIdOrThrow(),
            command.address,
            command.items.map {
                val product = products[it.productId] ?: throw IllegalArgumentException("not found product")
                OrderLineItem.of(product.getIdOrThrow(), product.price, it.quantity)
            }
        )
        order.place(userCoupon)
        saveOrderPort.saveOrder(order)
        userCoupon?.let { userCouponPort.saveUserCoupon(it) }
        eventPublisher.publishEvent(OrderPlacedEvent(order))
        return OrderInfo.from(order)
    }
}