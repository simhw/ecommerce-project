package com.ecommerce.order.command.domain.service

import com.ecommerce.order.command.application.`in`.FailOrderUseCase
import com.ecommerce.order.command.application.out.SaveOrderPort
import com.ecommerce.order.command.application.`in`.OrderInfo
import com.ecommerce.order.command.application.out.LoadOrderPort

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class FailOrderService(
    private val loadOrderPort: LoadOrderPort,
    private val saveOrderPort: SaveOrderPort
) : FailOrderUseCase {
    @Transactional
    override fun failOrder(number: String): OrderInfo {
        val order = loadOrderPort.loadOrderByNumber(number)
        order.fail()
        saveOrderPort.saveOrder(order)
        return OrderInfo.from(order)
    }
}