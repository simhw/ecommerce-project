package com.ecommerce.order

import com.ecommerce.account.adapter.out.persistence.AccountEntity
import com.ecommerce.common.model.Address
import com.ecommerce.common.model.Money
import com.ecommerce.coupon.command.adapter.out.persistence.entity.PercentDiscountCouponEntity
import com.ecommerce.order.command.adapter.out.persistence.entity.OrderEntity
import com.ecommerce.order.command.application.`in`.PlaceOrderCommand
import com.ecommerce.order.command.domain.model.OrderStatus
import com.ecommerce.order.command.domain.service.PlaceOrderService
import com.ecommerce.product.command.adapter.out.persistence.entity.ProductEntity
import com.ecommerce.product.command.adapter.out.persistence.entity.StockEntity
import com.ecommerce.product.command.domain.model.ProductStatus
import com.ecommerce.user.adapter.out.persistence.UserEntity
import com.ecommerce.usercoupon.command.adapter.out.persistence.UserCouponEntity
import com.ecommerce.usercoupon.command.domain.model.UserCouponStatus
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest
@Transactional
class PlaceOrderServiceIntegrationTest(
    @Autowired val placeOrderService: PlaceOrderService,
    @Autowired val em: EntityManager
) {
    @BeforeEach
    fun init() {
        val users = List(1) { i ->
            UserEntity.builder()
                .name("username")
                .email("email")
                .build()
        }
        val accounts = List(1) { i ->
            AccountEntity.builder()
                .balance(Money.ZERO)
                .user(users[i])
                .build()
        }
        users.map { em.persist(it) }
        accounts.map { em.persist(it) }

        val products = List(2) { i ->
            ProductEntity.builder()
                .name("product")
                .description("description")
                .price(Money.of(BigDecimal.valueOf(25000)))
                .status(ProductStatus.SELL)
                .build()
        }
        products.map { em.persist(it) }
        val stock = List(2) { i ->
            StockEntity.builder()
                .value(BigDecimal.valueOf(100))
                .productId(products[i].id)
                .updatedAt(LocalDateTime.now())
                .build()
        }
        stock.map { em.persist(it) }

        em.flush()
        em.clear()
    }

    @Test
    fun `주문 시 각 상품의 재고는 주문 수량만큼 차감된다`() {
        // given
        val command = PlaceOrderCommand(
            1L,
            address = Address("street", "city"),
            items = listOf(
                PlaceOrderCommand.OrderItem(1L, BigDecimal.valueOf(2))
            )
        )

        // when
        placeOrderService.placeOrder(command)

        // then
        val stock = em.find(StockEntity::class.java, 1L).value
        val orderEntity = em.find(OrderEntity::class.java, 1L)

        assertEquals(stock, BigDecimal.valueOf(98).setScale(2))
        assertEquals(orderEntity.status, OrderStatus.ORDERED)
    }

    @Test
    fun `주문 시 쿠폰을 적용한 경우 쿠폰 할인 정책만큼 할인 금액을 반환한다`() {
        // given
        val couponEntity = PercentDiscountCouponEntity.builder()
            .name("10% discount")
            .description("10% discount coupon")
//            .issueOfPeriod(DateTimePeriod(LocalDateTime.now(), LocalDateTime.now().plusMonths(1)))
//            .useOfPeriod(DateTimePeriod(LocalDateTime.now(), LocalDateTime.now().plusMonths(1)))
            .maxDiscountAmount(Money.of(BigDecimal.valueOf(10000)))
            .minOrderAmount(Money.of(BigDecimal.valueOf(10000)))
            .percent(BigDecimal.valueOf(0.1).setScale(2))
            .build()
        val userCouponEntity = UserCouponEntity.builder()
            .userId(1L)
            .coupon(couponEntity)
            .status(UserCouponStatus.UNUSED)
            .build()

        em.persist(couponEntity)
        em.persist(userCouponEntity)

        val command = PlaceOrderCommand(
            1L,
            address = Address("street", "city"),
            userCouponId = 1L,
            items = listOf(
                PlaceOrderCommand.OrderItem(1L, BigDecimal.valueOf(2))
            )
        )

        // when
        placeOrderService.placeOrder(command)

        // then
        val orderEntity = em.find(OrderEntity::class.java, 1L)

        val totalAmounts = BigDecimal.valueOf(23800 * 2).setScale(2)
        val totalDiscountAmounts = totalAmounts.multiply(BigDecimal.valueOf(0.1)).setScale(2)
        assertEquals(orderEntity.totalAmounts.getAmount().compareTo(totalAmounts), 0)
        assertEquals(orderEntity.totalDiscountAmounts.getAmount().compareTo(totalDiscountAmounts), 0)
    }
}