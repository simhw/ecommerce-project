package com.ecommerce.order

import com.ecommerce.common.model.Address
import com.ecommerce.order.command.application.`in`.PlaceOrderCommand
import com.ecommerce.order.command.domain.service.PlaceOrderService
import com.ecommerce.product.command.adapter.out.persistence.entity.StockEntity
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import java.math.BigDecimal
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors


@SpringBootTest
@Sql(scripts = ["/data/order-test-setup.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = ["/data/test-cleanup.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PlaceOrderServiceConcurrencyTest(
    @Autowired val placeOrderService: PlaceOrderService,
    @Autowired val em: EntityManager
) {
    companion object {
        const val THREAD_COUNT = 30
        const val STOCK_VALUE = 100L
    }

    @Test
    fun `동시에 30명이 같은 상품 주문 시 재고는 정합성을 유지해야한다`() {
        val executor = Executors.newFixedThreadPool(THREAD_COUNT)
        val latch = CountDownLatch(THREAD_COUNT)

        for (i in 1 until THREAD_COUNT + 1) {
            executor.submit {
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
        val stock1 = em.find(StockEntity::class.java, 1L).value
        val stock2 = em.find(StockEntity::class.java, 2L).value

        assertEquals(stock1, BigDecimal.valueOf(STOCK_VALUE - THREAD_COUNT).setScale(2))
        assertEquals(stock2, BigDecimal.valueOf(STOCK_VALUE - THREAD_COUNT).setScale(2))
    }
}