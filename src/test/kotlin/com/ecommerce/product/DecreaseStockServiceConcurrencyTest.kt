package com.ecommerce.product

import com.ecommerce.common.model.Money
import com.ecommerce.product.command.adapter.out.persistence.ProductJpaRepository
import com.ecommerce.product.command.adapter.out.persistence.StockJpaRepository
import com.ecommerce.product.command.adapter.out.persistence.entity.ProductEntity
import com.ecommerce.product.command.adapter.out.persistence.entity.StockEntity
import com.ecommerce.product.command.application.`in`.DecreaseStockCommand
import com.ecommerce.product.command.domain.model.ProductStatus
import com.ecommerce.product.command.domain.service.DecreaseStockService
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
class DecreaseStockServiceConcurrencyTest(
    @Autowired val decreaseStockService: DecreaseStockService,
    @Autowired val productJpaRepository: ProductJpaRepository,
    @Autowired val stockJpaRepository: StockJpaRepository,
    @Autowired val em: EntityManager
) {
    companion object {
        const val THREAD_COUNT = 15
        const val STOCK_VALUE = 10L
    }

    @BeforeEach
    fun init() {
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
    fun `동시에 재고 차감 시 재고는 정합성을 유지해야한다`() {
        val executor = Executors.newFixedThreadPool(THREAD_COUNT)
        val latch = CountDownLatch(THREAD_COUNT)

        for (i in 1..THREAD_COUNT) {
            executor.submit {
                val command = DecreaseStockCommand(
                    "ORD-TEST",
                    1L,
                    listOf(
                        DecreaseStockCommand.OrderItem(1L, BigDecimal.valueOf(1)),
                        DecreaseStockCommand.OrderItem(2L, BigDecimal.valueOf(1))
                    ),
                )
                // when
                try {
                    decreaseStockService.decreaseStock(command)
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
        val stock1 = em.find(StockEntity::class.java, 1L).value
        val stock2 = em.find(StockEntity::class.java, 2L).value

        assertEquals(stock1, BigDecimal.valueOf(STOCK_VALUE - THREAD_COUNT).setScale(2))
        assertEquals(stock2, BigDecimal.valueOf(STOCK_VALUE - THREAD_COUNT).setScale(2))
    }
}