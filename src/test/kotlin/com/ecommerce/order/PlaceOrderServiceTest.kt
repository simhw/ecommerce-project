package com.ecommerce.order

import com.ecommerce.common.model.Address
import com.ecommerce.common.model.DateTimePeriod
import com.ecommerce.common.model.Money
import com.ecommerce.coupon.command.domain.model.PercentDiscountCoupon
import com.ecommerce.order.command.application.`in`.PlaceOrderCommand
import com.ecommerce.order.command.application.out.SaveOrderPort
import com.ecommerce.order.command.domain.service.PlaceOrderService
import com.ecommerce.product.command.application.out.ProductPort
import com.ecommerce.product.command.application.out.StockPort
import com.ecommerce.product.command.domain.model.Product
import com.ecommerce.product.command.domain.model.ProductStatus
import com.ecommerce.product.command.domain.model.Stock
import com.ecommerce.user.application.out.LoadUserPort
import com.ecommerce.user.domain.model.User
import com.ecommerce.usercoupon.command.application.out.UserCouponPort
import com.ecommerce.usercoupon.command.domain.model.UserCoupon
import com.ecommerce.usercoupon.command.domain.model.UserCouponStatus
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.context.ApplicationEventPublisher
import java.math.BigDecimal
import java.time.LocalDateTime

class PlaceOrderServiceTest {
    private val loadUserPort = mockk<LoadUserPort>()
    private val userCouponPort = mockk<UserCouponPort>()
    private val productPort = mockk<ProductPort>()
    private val stockPort = mockk<StockPort>()
    private val saveOrderPort = mockk<SaveOrderPort>()
    private val eventPublisher = mockk<ApplicationEventPublisher>()

    private val placeOrderService =
        PlaceOrderService(loadUserPort, userCouponPort, productPort, saveOrderPort, eventPublisher)

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
            status = ProductStatus.SELL
        )
        val product2 = Product(
            2L,
            "name",
            "description",
            price = Money.of(BigDecimal.valueOf(18900)),
            status = ProductStatus.SELL
        )
        val stock1 = Stock(
            1L,
            BigDecimal.valueOf(100),
            1L,
            LocalDateTime.now()
        )
        val stock2 = Stock(
            2L,
            BigDecimal.valueOf(100),
            2L,
            LocalDateTime.now()
        )

        every { loadUserPort.loadUserBy(1L) } returns user
        every { productPort.loadProductBy(1L) } returns product1
        every { productPort.loadProductBy(2L) } returns product2
        every { stockPort.loadStockByProductId(1L) } returns stock1
        every { stockPort.loadStockByProductId(2L) } returns stock2
        every { saveOrderPort.saveOrder(any()) } just Runs
        every { stockPort.saveAllStock(any()) } just Runs

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
        assertEquals(BigDecimal.valueOf(99), stock1.value)
        assertEquals(BigDecimal.valueOf(98), stock2.value)
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
            status = ProductStatus.SELL
        )
        val product2 = Product(
            2L,
            "name",
            "description",
            price = Money.of(BigDecimal.valueOf(18900)),
            status = ProductStatus.SELL
        )
        val stock1 = Stock(
            1L,
            BigDecimal.valueOf(100),
            1L,
            LocalDateTime.now()
        )
        val stock2 = Stock(
            2L,
            BigDecimal.valueOf(100),
            2L,
            LocalDateTime.now()
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
        every { productPort.loadProductBy(1L) } returns product1
        every { productPort.loadProductBy(2L) } returns product2
        every { stockPort.loadStockByProductId(1L) } returns stock1
        every { stockPort.loadStockByProductId(2L) } returns stock2
        every { userCouponPort.loadUserCouponById(1L) } returns userCoupon
        every { saveOrderPort.saveOrder(any()) } just Runs
        every { stockPort.saveAllStock(any()) } just Runs
        every { userCouponPort.saveUserCoupon(any()) } just Runs

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