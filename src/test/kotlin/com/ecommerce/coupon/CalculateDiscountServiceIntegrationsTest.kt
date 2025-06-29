package com.ecommerce.coupon

import com.ecommerce.common.model.Money
import com.ecommerce.common.model.Period
import com.ecommerce.coupon.command.adapter.out.persistence.entity.*
import com.ecommerce.coupon.command.application.port.out.LoadCouponPort
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest
class CalculateDiscountServiceIntegrationsTest(
    @Autowired
    private val em: EntityManager,

    @Autowired
    private val port: LoadCouponPort
) {

    @Test
    @Transactional
    fun `할인 조건을 만족하는 경우, 할인 금액 계산 결과를 반환한다`() {
        // given
        val entity = AmountDiscountCouponEntity(
            name = "5000원 쿠폰",
            description = "5000원 할인",
            period = Period(
                LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59, 59)
            ),
            amount = Money.of(BigDecimal.valueOf(5_000)),
            condition = AllConditionEntity()
        )

        val condition1 = PeriodConditionEntity(
            period = Period(
                LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59, 59)
            )
        )

        val condition2 = AmountConditionEntity(
            amount = Money.of(BigDecimal.valueOf(10_000))
        )

        entity.condition.add(condition1)
        entity.condition.add(condition2)

        em.persist(entity)
        em.clear()
        em.flush()

        val expected = entity.amount
        val coupon = entity.id?.let { port.loadCouponBy(it) }
        // when
        val result = coupon?.calculateDiscount(Money.of(BigDecimal.valueOf(10_000)))

        // then
        Assertions.assertThat(result).isEqualTo(expected)
    }


    @Test
    @Transactional
    fun `기간 조건을 만족하지 못하는 경우, 할인 금액 계산 결과로 '0'을 반환한다`() {
        // given
        val entity = AmountDiscountCouponEntity(
            name = "5000원 쿠폰",
            description = "5000원 할인",
            period = Period(
                LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59, 59)
            ),
            amount = Money.of(BigDecimal.valueOf(5_000)),
            condition = AllConditionEntity()
        )

        val condition = PeriodConditionEntity(
            period = Period(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusSeconds(1)
            )
        )

        entity.condition.add(condition)

        em.persist(entity)

        val coupon = entity.id?.let { port.loadCouponBy(it) }

        // when
        val result = coupon?.calculateDiscount(Money.of(BigDecimal.valueOf(10_000)))

        // then
        Assertions.assertThat(result).isEqualTo(Money.ZERO)
    }
}