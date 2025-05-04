package com.ecommerce.order

import com.ecommerce.common.model.Address
import com.ecommerce.common.model.DateTimePeriod
import com.ecommerce.common.model.Money
import com.ecommerce.coupon.domain.model.PercentDiscountCoupon
import com.ecommerce.order.command.application.`in`.PlaceOrderCommand
import com.ecommerce.order.command.application.out.SaveOrderPort
import com.ecommerce.order.command.domain.service.PlaceOrderService
import com.ecommerce.product.command.application.out.LoadProductPort
import com.ecommerce.product.command.application.out.SaveProductPort
import com.ecommerce.product.command.domain.model.Product
import com.ecommerce.product.command.domain.model.ProductStatus
import com.ecommerce.product.command.domain.model.ProductStock
import com.ecommerce.user.application.out.LoadUserPort
import com.ecommerce.user.domain.model.User
import com.ecommerce.usercoupon.command.application.out.LoadUserCouponPort
import com.ecommerce.usercoupon.command.application.out.SaveUserCouponPort
import com.ecommerce.usercoupon.command.domain.model.UserCoupon
import com.ecommerce.usercoupon.command.domain.model.UserCouponStatus
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.math.BigDecimal
import java.time.LocalDateTime

class PlaceOrderServiceTest {
    private val loadUserPort = mockk<LoadUserPort>()
    private val loadUserCouponPort = mockk<LoadUserCouponPort>()
    private val loadProductPort = mockk<LoadProductPort>()
    private val saveOrderPort = mockk<SaveOrderPort>()
    private val saveUserCouponPort = mockk<SaveUserCouponPort>()
    private val saveProductPort = mockk<SaveProductPort>()

    private val placeOrderService = PlaceOrderService(
        loadUserPort,
        loadUserCouponPort,
        loadProductPort,
        saveOrderPort,
        saveUserCouponPort,
        saveProductPort
    )

    @Test
    fun `주문 시 각 상품의 재고는 주문 수량만큼 차감된다`() {
        // given
        val user = User(
            id = 1L,
            email = "email",
            name = "username",
            createdAt = LocalDateTime.now()
        )
        val product1 = Product(
            1L,
            "name",
            "description",
            price = Money.of(BigDecimal.valueOf(23800)),
            stock = ProductStock(1L, BigDecimal.valueOf(100), LocalDateTime.now()),
            status = ProductStatus.SELL
        )
        val product2 = Product(
            2L,
            "name",
            "description",
            price = Money.of(BigDecimal.valueOf(18900)),
            stock = ProductStock(2L, BigDecimal.valueOf(200), LocalDateTime.now()),
            status = ProductStatus.SELL
        )

        every { loadUserPort.loadUserBy(1L) } returns user
        every { loadProductPort.loadProductBy(1L) } returns product1
        every { loadProductPort.loadProductBy(2L) } returns product2
        every { saveOrderPort.saveOrder(any()) } just Runs
        every { saveProductPort.saveProduct(any()) } just Runs

        val command = PlaceOrderCommand(
            1L,
            address = Address("street", "city"),
            userCouponId = null,
            items = listOf(
                PlaceOrderCommand.OrderItem(1L, BigDecimal.valueOf(1)),
                PlaceOrderCommand.OrderItem(2L, BigDecimal.valueOf(2))
            )
        )

        // when
        placeOrderService.placeOrder(command)

        // then
        assertEquals(BigDecimal.valueOf(99), product1.stock.value)
        assertEquals(BigDecimal.valueOf(198), product2.stock.value)
    }

    @Test
    fun `주문 시 쿠폰을 소지한 경우 쿠폰 할인 정책만큼 총 할인 금액을 계산한다`() {
        // given
        val user = User(
            id = 1L,
            email = "email",
            name = "username",
            createdAt = LocalDateTime.now()
        )
        val product1 = Product(
            1L,
            "name",
            "description",
            price = Money.of(BigDecimal.valueOf(23800)),
            stock = ProductStock(1L, BigDecimal.valueOf(100), LocalDateTime.now()),
            status = ProductStatus.SELL
        )
        val product2 = Product(
            2L,
            "name",
            "description",
            price = Money.of(BigDecimal.valueOf(18900)),
            stock = ProductStock(2L, BigDecimal.valueOf(200), LocalDateTime.now()),
            status = ProductStatus.SELL
        )
        val coupon = PercentDiscountCoupon(
            id = 1L,
            name = "10% discount coupon",
            "description",
            minOrderAmount = Money.of(BigDecimal.valueOf(50000)),
            maxDiscountAmount = Money.of(BigDecimal.valueOf(10000)),
            useOfPeriod = DateTimePeriod(LocalDateTime.now(), LocalDateTime.now().plusDays(1)),
            issueOfPeriod = DateTimePeriod(LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1)),
            percent = BigDecimal.valueOf(0.1)
        )
        val userCoupon = UserCoupon(
            id = 1L,
            userId = 1L,
            coupon = coupon,
            status = UserCouponStatus.UNUSED
        )

        every { loadUserPort.loadUserBy(1L) } returns user
        every { loadProductPort.loadProductBy(1L) } returns product1
        every { loadProductPort.loadProductBy(2L) } returns product2
        every { loadUserCouponPort.loadUserCouponBy(1L) } returns userCoupon
        every { saveOrderPort.saveOrder(any()) } just Runs
        every { saveProductPort.saveProduct(any()) } just Runs
        every { saveUserCouponPort.saveUserCoupon(any()) } just Runs

        val command = PlaceOrderCommand(
            1L,
            address = Address("street", "city"),
            userCouponId = 1L,
            items = listOf(
                PlaceOrderCommand.OrderItem(1L, BigDecimal.valueOf(1)),
                PlaceOrderCommand.OrderItem(2L, BigDecimal.valueOf(2))
            )
        )

        // when
        val orderInfo = placeOrderService.placeOrder(command)

        // then
        assertEquals(orderInfo.totalAmounts.getAmount(), BigDecimal.valueOf(61600))
        assertEquals(orderInfo.totalDiscountAmounts.getAmount(), BigDecimal.valueOf(6160.0))
    }
}