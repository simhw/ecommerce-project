package com.ecommerce.usercoupon

import com.ecommerce.common.model.DateTimePeriod
import com.ecommerce.common.model.Money
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
    @Autowired val issueCouponService: IssueCouponService,
    @Autowired val entityManager: EntityManager
) {
    @BeforeEach
    fun init() {
        val userEntity = UserEntity.builder()
            .name("username")
            .email("email")
            .build()
        val couponEntity = PercentDiscountCouponEntity.builder()
            .name("10% discount")
            .description("10% discount coupon")
            .issueOfPeriod(DateTimePeriod(LocalDateTime.now(), LocalDateTime.now().plusMonths(1)))
            .useOfPeriod(DateTimePeriod(LocalDateTime.now(), LocalDateTime.now().plusMonths(1)))
            .maxDiscountAmount(Money.of(BigDecimal.valueOf(10000)))
            .minOrderAmount(Money.of(BigDecimal.valueOf(10000)))
            .percent(BigDecimal.valueOf(0.1).setScale(2))
            .build()

        entityManager.persist(userEntity)
        entityManager.persist(couponEntity)
        entityManager.flush()
        entityManager.clear()
    }

    @Test
    fun `쿠폰 발급 성공`() {
        // given
        val command = IssueCouponCommand(1L, 1L)

        // when
        issueCouponService.issueCoupon(command)

        // then
        val userCouponEntity = entityManager.find(UserCouponEntity::class.java, 1L)

        assertEquals(userCouponEntity.userId, 1L)
        assertEquals(userCouponEntity.status, UserCouponStatus.UNUSED)
    }
}