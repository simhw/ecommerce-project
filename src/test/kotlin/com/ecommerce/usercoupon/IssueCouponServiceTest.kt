package com.ecommerce.usercoupon

import com.ecommerce.common.model.Money
import com.ecommerce.common.model.Period
import com.ecommerce.coupon.command.application.port.out.LoadCouponPort
import com.ecommerce.coupon.command.domain.NoneCondition
import com.ecommerce.coupon.command.domain.PercentDiscountCoupon
import com.ecommerce.user.application.out.LoadUserPort
import com.ecommerce.user.domain.model.User
import com.ecommerce.usercoupon.command.application.`in`.IssueCouponCommand
import com.ecommerce.usercoupon.command.application.out.UserCouponPort
import com.ecommerce.usercoupon.command.domain.service.IssueCouponService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

import java.math.BigDecimal
import java.time.LocalDateTime

class IssueCouponServiceTest {
    private val loadUserPort = mockk<LoadUserPort>()
    private val loadCouponPort = mockk<LoadCouponPort>()
    private val userCouponPort = mockk<UserCouponPort>()

    private val issueCouponService = IssueCouponService(loadUserPort, loadCouponPort, userCouponPort)

    @Test
    fun `발급이 만료된 쿠폰 발급 시 IllegalArgumentException 발생`() {
        // given
        val user = User(1L, "email", "username", LocalDateTime.now(), null)
        val coupon = PercentDiscountCoupon(
            id = 1,
            name = "coupon",
            description = "description",
            period = Period(
                LocalDateTime.now().minusDays(7),
                LocalDateTime.now().minusDays(1)
            ),
            condition = NoneCondition(),
            percent = BigDecimal.valueOf(0.1),
            maxDiscountAmount = Money.of(BigDecimal.valueOf(1000))
        )
        val command = IssueCouponCommand(1L, 1L)

        every { loadUserPort.loadUserBy(1L) } returns user
        every { loadCouponPort.loadCouponBy(1L) } returns coupon

        // when, then
        assertThatThrownBy { issueCouponService.issueCoupon(command) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}