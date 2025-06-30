package com.ecommerce.usercoupon

import com.ecommerce.common.model.Money
import com.ecommerce.common.model.Period
import com.ecommerce.coupon.command.adapter.out.persistence.entity.NoneConditionEntity
import com.ecommerce.coupon.command.adapter.out.persistence.entity.PercentDiscountCouponEntity
import com.ecommerce.user.adapter.out.persistence.UserEntity
import com.ecommerce.usercoupon.command.adapter.out.persistence.UserCouponEntity
import com.ecommerce.usercoupon.command.application.`in`.IssueCouponCommand
import com.ecommerce.usercoupon.command.domain.model.UserCouponStatus
import com.ecommerce.usercoupon.command.domain.service.IssueCouponService
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest
@Transactional
class IssueCouponIntegrationTest(
    @Autowired val service: IssueCouponService,
    @Autowired val em: EntityManager
) {
    @BeforeEach
    fun init() {
        val user = UserEntity.builder()
            .name("username")
            .email("email")
            .build()

        val coupon = PercentDiscountCouponEntity(
            null,
            "10% 쿠폰",
            "10% 할인",
            Period(LocalDateTime.now(), LocalDateTime.now().plusDays(1)),
            NoneConditionEntity(null, null, null),
            BigDecimal.valueOf(0.1).setScale(2),
            Money(BigDecimal.valueOf(10000))
        )

        em.persist(user)
        em.persist(coupon)
        em.flush()
        em.clear()
    }

    @Test
    fun `쿠폰 발급 성공`() {
        // given
        val command = IssueCouponCommand(1L, 1L)

        // when
        service.issueCoupon(command)

        // then
        val usercoupon = em.find(UserCouponEntity::class.java, 1L)
        assertEquals(usercoupon.userId, 1L)
        assertEquals(usercoupon.status, UserCouponStatus.UNUSED)
    }
}