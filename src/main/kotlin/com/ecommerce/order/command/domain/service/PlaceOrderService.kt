package com.ecommerce.order.command.domain.service

import com.ecommerce.order.command.application.out.SaveOrderPort
import com.ecommerce.order.command.application.`in`.OrderInfo
import com.ecommerce.order.command.application.`in`.PlaceOrderUseCase
import com.ecommerce.order.command.application.`in`.PlaceOrderCommand
import com.ecommerce.order.command.domain.model.Order
import com.ecommerce.order.command.domain.model.OrderLineItem
import com.ecommerce.product.command.application.out.LoadProductPort
import com.ecommerce.product.command.application.out.SaveProductPort
import com.ecommerce.user.application.out.LoadUserPort
import com.ecommerce.usercoupon.command.application.out.LoadUserCouponPort
import com.ecommerce.usercoupon.command.application.out.SaveUserCouponPort
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PlaceOrderService(
    val loadUserPort: LoadUserPort,
    val loadUserCouponPort: LoadUserCouponPort,
    val loadProductPort: LoadProductPort,
    val saveOrderPort: SaveOrderPort,
    val saveUserCouponPort: SaveUserCouponPort,
    val saveProductPort: SaveProductPort,
) : PlaceOrderUseCase {

    @Transactional
    override fun placeOrder(command: PlaceOrderCommand): OrderInfo {
        val user = loadUserPort.loadUserBy(command.userId)
        val products = command.items.map { loadProductPort.loadProductBy(it.productId) }
        val items = command.items.mapIndexed { idx, it ->
            OrderLineItem.register(it.quantity, products[idx])
        }
        val userCoupon = command.userCouponId?.let { loadUserCouponPort.loadUserCouponBy(it) }

        val order = Order.of(user, command.address, items)
        order.place(userCoupon)

        items.forEach { saveProductPort.saveProduct(it.product) }
        userCoupon?.let { saveUserCouponPort.saveUserCoupon(it) }

        val savedOrder = saveOrderPort.saveOrder(order)
        return OrderInfo.from(savedOrder)    }
}