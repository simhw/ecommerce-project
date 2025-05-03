package com.ecommerce.payment.domain.service

import com.ecommerce.account.application.port.out.LoadAccountPort
import com.ecommerce.account.application.port.out.SaveAccountPort
import com.ecommerce.order.command.application.out.LoadOrderPort
import com.ecommerce.order.command.application.out.SaveOrderPort
import com.ecommerce.payment.applicaiton.`in`.PayOrderCommand
import com.ecommerce.payment.applicaiton.`in`.PayOrderUseCase
import com.ecommerce.payment.applicaiton.`in`.PaymentInfo
import com.ecommerce.payment.applicaiton.out.SavePaymentPort
import com.ecommerce.payment.domain.model.Payment
import com.ecommerce.user.application.out.LoadUserPort
import org.springframework.stereotype.Service

@Service
class PayOrderService(
    private val loadOrderPort: LoadOrderPort,
    private val loadUserPort: LoadUserPort,
    private val loadAccountPort: LoadAccountPort,
    private val savePaymentPort: SavePaymentPort,
    private val saveAccountPort: SaveAccountPort,
    private val saveOrderPort: SaveOrderPort
) : PayOrderUseCase {
    override fun payOrder(command: PayOrderCommand): PaymentInfo {
        val user = loadUserPort.loadUserBy(command.userId)
        val account = loadAccountPort.loadAccountBy(user)
        val order = loadOrderPort.loadOrderBy(command.orderNumber)

        val payment = Payment.of(order, user)
        payment.pay(account)

        savePaymentPort.savePayment(payment)
        saveAccountPort.saveAccount(account)
        saveOrderPort.saveOrder(order)
        return PaymentInfo.of(payment)
    }
}