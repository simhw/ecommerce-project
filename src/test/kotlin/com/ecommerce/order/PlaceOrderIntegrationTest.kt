package com.ecommerce.order

import com.ecommerce.account.adapter.out.persistence.AccountEntity
import com.ecommerce.common.model.Address
import com.ecommerce.common.model.DateTimePeriod
import com.ecommerce.common.model.Money
import com.ecommerce.coupon.command.adapter.out.persistence.entity.PercentDiscountCouponEntity
import com.ecommerce.order.command.adapter.out.persistence.entity.OrderEntity
import com.ecommerce.order.command.application.`in`.PlaceOrderCommand
import com.ecommerce.order.command.domain.model.OrderStatus
import com.ecommerce.order.command.domain.service.PlaceOrderService
import com.ecommerce.product.command.adapter.out.persistence.entity.ProductEntity
import com.ecommerce.product.command.adapter.out.persistence.entity.ProductStockEntity
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
class PlaceOrderIntegrationTest(
    @Autowired val placeOrderService: PlaceOrderService,
    @Autowired val entityManager: EntityManager
) {
    @BeforeEach
    fun init() {
        val userEntity = UserEntity.builder()
            .name("username")
            .email("email")
            .build()
        val accountEntity = AccountEntity.builder()
            .balance(Money.ZERO)
            .user(userEntity)
            .build()
        val productStockEntity = ProductStockEntity.builder()
            .value(BigDecimal.valueOf(100))
            .updatedAt(LocalDateTime.now())
            .build()
        val productEntity = ProductEntity.builder()
            .name("product")
            .description("description")
            .price(Money.of(BigDecimal.valueOf(23800)))
            .status(ProductStatus.SELL)
            .build()
        productEntity.setStock(productStockEntity)

        entityManager.persist(userEntity)
        entityManager.persist(accountEntity)
        entityManager.persist(productEntity)

        entityManager.flush()
        entityManager.clear()
    }

    @Test
    fun `주문 시 각 상품의 재고는 주문 수량만큼 차감된다`() {
        // given
        val command = PlaceOrderCommand(
            1L,
            address = Address("street", "city"),
            userCouponId = null,
            items = listOf(
                PlaceOrderCommand.OrderItem(1L, BigDecimal.valueOf(2))
            )
        )

        // when
        placeOrderService.placeOrder(command)

        // then
        val productEntity = entityManager.find(ProductEntity::class.java, 1L)
        val orderEntity = entityManager.find(OrderEntity::class.java, 1L)

        assertEquals(productEntity.stock.getValue(), BigDecimal.valueOf(98).setScale(2))
        assertEquals(orderEntity.status, OrderStatus.ORDERED)
    }

    @Test
    fun `주문 시 쿠폰을 적용한 경우 쿠폰 할인 정책만큼 할인 금액을 반환한다`() {
        // given
        val couponEntity = PercentDiscountCouponEntity.builder()
            .name("10% discount")
            .description("10% discount coupon")
            .issueOfPeriod(DateTimePeriod(LocalDateTime.now(), LocalDateTime.now().plusMonths(1)))
            .useOfPeriod(DateTimePeriod(LocalDateTime.now(), LocalDateTime.now().plusMonths(1)))
            .maxDiscountAmount(Money.of(BigDecimal.valueOf(10000)))
            .minOrderAmount(Money.of(BigDecimal.valueOf(10000)))
            .percent(BigDecimal.valueOf(0.1).setScale(2))
            .build()
        val userCouponEntity = UserCouponEntity.builder()
            .userId(1L)
            .coupon(couponEntity)
            .status(UserCouponStatus.UNUSED)
            .build()

        entityManager.persist(couponEntity)
        entityManager.persist(userCouponEntity)

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
        val orderEntity = entityManager.find(OrderEntity::class.java, 1L)

        val totalAmounts = BigDecimal.valueOf(23800 * 2).setScale(2)
        val totalDiscountAmounts = totalAmounts.multiply(BigDecimal.valueOf(0.1)).setScale(2)
        assertEquals(orderEntity.totalAmounts.getAmount().compareTo(totalAmounts), 0)
        assertEquals(orderEntity.totalDiscountAmounts.getAmount().compareTo(totalDiscountAmounts), 0)
    }
}