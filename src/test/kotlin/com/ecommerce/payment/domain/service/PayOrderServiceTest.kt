package com.ecommerce.payment.domain.service

import com.ecommerce.account.application.port.out.LoadAccountPort
import com.ecommerce.account.application.port.out.SaveAccountPort
import com.ecommerce.account.domain.model.Account
import com.ecommerce.common.model.Address
import com.ecommerce.common.model.Money
import com.ecommerce.order.command.application.out.LoadOrderPort
import com.ecommerce.order.command.application.out.SaveOrderPort
import com.ecommerce.order.command.domain.model.Order
import com.ecommerce.order.command.domain.model.OrderStatus
import com.ecommerce.payment.applicaiton.`in`.PayOrderCommand
import com.ecommerce.payment.applicaiton.out.SavePaymentPort
import com.ecommerce.user.application.out.LoadUserPort
import com.ecommerce.user.domain.model.User
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.math.BigDecimal
import java.time.LocalDateTime

class PayOrderServiceTest {
    private val loadOrderPort = mockk<LoadOrderPort>()
    private val loadUserPort = mockk<LoadUserPort>()
    private val loadAccountPort = mockk<LoadAccountPort>()
    private val savePaymentPort = mockk<SavePaymentPort>()
    private val saveAccountPort = mockk<SaveAccountPort>()
    private val saveOrderPort = mockk<SaveOrderPort>()

    private val payOrderService = PayOrderService(
        loadOrderPort,
        loadUserPort,
        loadAccountPort,
        savePaymentPort,
        saveAccountPort,
        saveOrderPort
    )

    @Test
    fun `주문 결제 시 결제 금액을 반환한다`() {
        // given
        val user = User(
            id = 1L,
            email = "email",
            name = "username",
            createdAt = LocalDateTime.now()
        )
        val account = Account(
            id = 1L,
            balance = Money.of(BigDecimal.valueOf(10000)),
            user = user,
            updatedAt = LocalDateTime.now()
        )
        val order = Order(
            id = 1L,
            number = "ORD-TEST",
            userId = 1L,
            address = Address("street", "city"),
            items = listOf(),
            totalAmounts = Money.of(BigDecimal.valueOf(10000)),
            totalDiscountAmounts = Money.of(BigDecimal.valueOf(1000)),
            status = OrderStatus.ORDERED,
            createdAt = LocalDateTime.now()
        )

        every { loadUserPort.loadUserBy(1L) } returns user
        every { loadOrderPort.loadOrderBy("ORD-TEST") } returns order
        every { loadAccountPort.loadAccountBy(user) } returns account

        every { saveOrderPort.saveOrder(any()) } just Runs
        every { savePaymentPort.savePayment(any()) } just Runs
        every { saveAccountPort.saveAccount(any()) } just Runs

        val command = PayOrderCommand("ORD-TEST", 1)

        // when
        val paymentInfo = payOrderService.payOrder(command)

        // then
        assertEquals(paymentInfo.amount.getAmount(), BigDecimal.valueOf(9000))
        assertEquals(order.status, OrderStatus.PAID)
        assertEquals(account.balance.getAmount(), BigDecimal.valueOf(1000))
    }

    @Test
    fun `주문 결제 시 잔고가 부족한 경우 ArithmeticException 발생`() {
        // given
        val user = User(
            id = 1L,
            email = "email",
            name = "username",
            createdAt = LocalDateTime.now()
        )
        val account = Account(
            id = 1L,
            balance = Money.of(BigDecimal.valueOf(10000)),
            user = user,
            updatedAt = LocalDateTime.now()
        )
        val order = Order(
            id = 1L,
            number = "ORD-TEST",
            userId = 1L,
            address = Address("street", "city"),
            items = listOf(),
            totalAmounts = Money.of(BigDecimal.valueOf(12000)),
            totalDiscountAmounts = Money.of(BigDecimal.valueOf(1000)),
            status = OrderStatus.ORDERED,
            createdAt = LocalDateTime.now()
        )

        every { loadUserPort.loadUserBy(1L) } returns user
        every { loadOrderPort.loadOrderBy("ORD-TEST") } returns order
        every { loadAccountPort.loadAccountBy(user) } returns account

        every { saveOrderPort.saveOrder(any()) } just Runs
        every { savePaymentPort.savePayment(any()) } just Runs
        every { saveAccountPort.saveAccount(any()) } just Runs

        val command = PayOrderCommand("ORD-TEST", 1)

        // when, then
        assertThatThrownBy { payOrderService.payOrder(command) }
            .isInstanceOf(ArithmeticException::class.java)
    }
}