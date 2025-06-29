package com.ecommerce.coupon

import com.ecommerce.common.model.Money
import com.ecommerce.common.model.Period
import com.ecommerce.coupon.command.adapter.out.persistence.CouponEntityMapper
import com.ecommerce.coupon.command.adapter.out.persistence.entity.*
import com.ecommerce.coupon.command.domain.AnyCondition
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.LocalDateTime

import kotlin.test.Test

@SpringBootTest
class CouponEntityMapperTest(
    @Autowired
    private val em: EntityManager,

    @Autowired
    private val mapper: CouponEntityMapper
) {
    @Test
    fun `쿠폰 엔티티 도메인 객체로 변환`() {
        val condition = AnyConditionEntity(
            children = mutableListOf()
        )
        condition.add(
            PeriodConditionEntity(
                id = null,
                parent = null,
                children = mutableListOf(),
                period = Period(LocalDateTime.now(), LocalDateTime.now().plusDays(1))
            )
        )
        condition.add(
            AmountConditionEntity(
                id = null,
                parent = null,
                children = mutableListOf(),
                amount = Money.of(BigDecimal.valueOf(10_000))
            )
        )

        val entity = AmountDiscountCouponEntity(
            id = null,
            name = "1,000원 쿠폰",
            description = "1000원 할인",
            period = Period(LocalDateTime.now(), LocalDateTime.now().plusDays(1)),
            condition = condition,
            amount = Money.of(BigDecimal.valueOf(1_000))
        )

        val domain = mapper.to(entity)
        val discounted = domain.getDiscountAmount(Money.of(BigDecimal.valueOf(10_000)))
        Assertions.assertThat(discounted).isEqualTo(Money.of(BigDecimal.valueOf(1_000)))
    }

    @Test
    @Transactional
    fun `쿠폰 엔티티 조회 후 도메인 객체로 변환`() {
        val condition = AnyConditionEntity(
            children = mutableListOf()
        )
        condition.add(
            PeriodConditionEntity(
                period = Period(LocalDateTime.now(), LocalDateTime.now().plusDays(1))
            )
        )
        condition.add(
            AmountConditionEntity(
                amount = Money.of(BigDecimal.valueOf(10_000))
            )
        )

        val entity = AmountDiscountCouponEntity(
            name = "1,000원 쿠폰",
            description = "1000원 할인",
            period = Period(LocalDateTime.now(), LocalDateTime.now().plusDays(1)),
            condition = condition,
            amount = Money.of(BigDecimal.valueOf(1_000))
        )

        em.persist(entity)
        em.clear()
        em.flush()

        val find = em.find(CouponEntity::class.java, entity.id!!)
        val domain = mapper.to(find)
        Assertions.assertThat(domain.condition).isInstanceOf(AnyCondition::class.java)
    }
}