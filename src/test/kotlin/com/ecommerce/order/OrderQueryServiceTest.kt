package com.ecommerce.order

import com.ecommerce.common.model.Money
import com.ecommerce.order.command.adapter.out.persistence.entity.OrderEntity
import com.ecommerce.order.command.adapter.out.persistence.entity.OrderLineItemEntity
import com.ecommerce.order.command.domain.model.OrderStatus
import com.ecommerce.order.query.application.OrderQueryService
import com.ecommerce.order.query.application.SearchTopSellerCriteria
import com.ecommerce.product.command.adapter.out.persistence.entity.ProductEntity
import com.ecommerce.product.command.adapter.out.persistence.entity.StockEntity
import com.ecommerce.product.command.domain.model.ProductStatus
import com.ecommerce.user.adapter.out.persistence.UserEntity
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
@Transactional
class OrderQueryServiceTest(
    @Autowired private val orderQueryService: OrderQueryService,
    @Autowired private val entityManager: EntityManager
) {
    @BeforeEach
    fun init() {
        val random = Random()
        for (i in 1..10) {
            val userEntity = UserEntity.builder()
                .name("username$i")
                .email("email")
                .build()
            entityManager.persist(userEntity)
        }
        for (i in 1..100) {
            val stockEntity = StockEntity.builder()
                .value(BigDecimal.valueOf(100))
                .updatedAt(LocalDateTime.now())
                .build()
            val productEntity = ProductEntity.builder()
                .name("product$i")
                .description("description")
                .price(Money.of(BigDecimal.valueOf(10000)))
                .status(ProductStatus.SELL)
                .build()
            entityManager.persist(stockEntity)
            entityManager.persist(productEntity)
        }
        for (i in 1..1000) {
            val orderLineItemEntity = OrderLineItemEntity.builder()
                .productId(random.nextInt(100).toLong() + 1)
                .price(Money.of(BigDecimal.valueOf(10000)))
                .quantity(BigDecimal.valueOf(2))
                .build()
            val orderEntity = OrderEntity.builder()
                .number("ORD-$i")
                .userId(random.nextInt(10).toLong() + 1)
                .items(ArrayList())
                .totalAmounts(Money.of(BigDecimal.valueOf(20000)))
                .status(OrderStatus.ORDERED)
                .createdAt(LocalDateTime.now())
                .build()
            orderEntity.addItem(orderLineItemEntity)
            entityManager.persist(orderEntity)
        }
    }

    @Test
    fun `주문 수량 통계 검색`() {
        val criteria = SearchTopSellerCriteria(LocalDate.now().minusDays(2), LocalDate.now())
        val topSellers = orderQueryService.searchTopSellers(criteria)

        assertEquals(topSellers?.size, 30)
    }
}