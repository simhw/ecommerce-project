package com.ecommerce.order

import com.ecommerce.common.model.Address
import com.ecommerce.common.model.Money
import com.ecommerce.order.command.application.`in`.PlaceOrderCommand
import com.ecommerce.order.command.domain.service.PlaceOrderService
import com.ecommerce.product.command.adapter.out.persistence.ProductJpaRepository
import com.ecommerce.product.command.adapter.out.persistence.StockJpaRepository
import com.ecommerce.product.command.adapter.out.persistence.entity.ProductEntity
import com.ecommerce.product.command.adapter.out.persistence.entity.StockEntity
import com.ecommerce.product.command.domain.model.ProductStatus
import com.ecommerce.user.adapter.out.persistence.UserEntity
import com.ecommerce.user.adapter.out.persistence.UserJpaRepository
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class PlaceOrderServiceConcurrencyTest(
    @Autowired val placeOrderService: PlaceOrderService,
    @Autowired val userJpaRepository: UserJpaRepository,
    @Autowired val productJpaRepository: ProductJpaRepository,
    @Autowired val stockJpaRepository: StockJpaRepository,
    @Autowired val em: EntityManager
) {
    companion object {
        const val THREAD_COUNT = 30
        const val STOCK_VALUE = 20L
    }

    @BeforeEach
    fun init() {
        for (i in 1..THREAD_COUNT) {
            userJpaRepository.save(
                UserEntity.builder()
                    .name("username${i}")
                    .email("username@email.com")
                    .build()
            )
        }
        for (i in 1..2) {
            productJpaRepository.save(
                ProductEntity.builder()
                    .name("product${i}")
                    .description("description")
                    .price(Money.of(BigDecimal.valueOf(10000)))
                    .status(ProductStatus.SELL)
                    .build()
            )
        }
        for (i in 1..2) {
            stockJpaRepository.save(
                StockEntity.builder()
                    .productId(i.toLong())
                    .value(BigDecimal.valueOf(STOCK_VALUE))
                    .updatedAt(LocalDateTime.now())
                    .build()
            )
        }
    }

    @Test
    fun `동시에 30명이 같은 상품 주문 시 재고는 정합성을 유지해야한다`() {
        val executor = Executors.newFixedThreadPool(THREAD_COUNT)
        val latch = CountDownLatch(THREAD_COUNT)

        for (i in 1..THREAD_COUNT) {
            executor.submit {
                // given
                val command = PlaceOrderCommand(
                    userId = i.toLong(),
                    address = Address("street", "city"),
                    items = listOf(
                        PlaceOrderCommand.OrderItem(1L, BigDecimal.valueOf(1)),
                        PlaceOrderCommand.OrderItem(2L, BigDecimal.valueOf(1))
                    ),
                )
                // when
                try {
                    placeOrderService.placeOrder(command)
                } catch (e: Exception) {
                    println("error ${e.message}")
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await()
        executor.shutdown();

        // then
        val result = em.createQuery("select count(1) from OrderEntity o").singleResult
        assertEquals(result.toString().toInt(), THREAD_COUNT)
    }
}