package com.ecommerce.order.command.domain.service

import com.ecommerce.order.command.application.out.SaveOrderPort
import com.ecommerce.order.command.application.`in`.OrderInfo
import com.ecommerce.order.command.application.`in`.PlaceOrderUseCase
import com.ecommerce.order.command.application.`in`.PlaceOrderCommand
import com.ecommerce.order.command.domain.model.Order
import com.ecommerce.order.command.domain.model.OrderLineItem
import com.ecommerce.product.command.application.out.ProductPort
import com.ecommerce.product.command.application.out.StockPort

import com.ecommerce.user.application.out.LoadUserPort
import com.ecommerce.usercoupon.command.application.out.UserCouponPort
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PlaceOrderService(
    val loadUserPort: LoadUserPort,
    val userCouponPort: UserCouponPort,
    val productPort: ProductPort,
    val stockPort: StockPort,
    val saveOrderPort: SaveOrderPort
) : PlaceOrderUseCase {
    @Transactional
    override fun placeOrder(command: PlaceOrderCommand): OrderInfo {
        // 유효 회원 검증
        val user = loadUserPort.loadUserBy(command.userId)
            .also { it.verifyActiveUser() }
        // 상품 판매 상태 검증
        val products = command.items
            .map { productPort.loadProductBy(it.productId) }
            .onEach { it.verifyAvailableForSale() }
            .associateBy { it.getIdOrThrow() }
        val stocks = command.items
            .map { stockPort.loadStockForUpdateByProductId(it.productId) }
            .associateBy { it.productId }
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
        command.items.map {
            val stock = stocks[it.productId] ?: throw IllegalArgumentException("not found stock")
            println("stock.value = ${stock.value}")
            stock.decrease(it.quantity)
        }

        stockPort.saveAllStock(stocks.values.toList())
        saveOrderPort.saveOrder(order)
        userCoupon?.let { userCouponPort.saveUserCoupon(it) }
        return OrderInfo.from(order)
    }
}