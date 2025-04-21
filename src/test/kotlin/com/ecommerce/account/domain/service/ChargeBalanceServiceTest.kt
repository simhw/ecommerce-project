package com.ecommerce.account.domain.service

import com.ecommerce.account.application.port.`in`.ChargeBalanceCommand
import com.ecommerce.account.application.port.out.LoadAccountPort
import com.ecommerce.account.application.port.out.UpdateAccountPort
import com.ecommerce.account.domain.model.Account
import com.ecommerce.common.model.Money
import com.ecommerce.user.application.out.LoadUserPort
import com.ecommerce.user.domain.model.User
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class ChargeBalanceServiceTest {
    private val loadAccountPort = mockk<LoadAccountPort>()
    private val updateAccountPort = mockk<UpdateAccountPort>(relaxed = true)
    private val loadUserPort = mockk<LoadUserPort>()

    private val chargeBalanceService = ChargeBalanceService(loadUserPort, loadAccountPort, updateAccountPort)

    @Test
    fun `잔액이 충전 금액만큼 증가한다`() {
        // given
        val amount = BigDecimal.valueOf(1000)
        val command = ChargeBalanceCommand(1L, amount)

        val user = User(1L, "email", "username", LocalDateTime.now(), null)
        val account = Account(1L, Money(BigDecimal.valueOf(10000)), user, null, LocalDateTime.now())

        every { loadUserPort.loadUserBy(1L) } returns user
        every { loadAccountPort.loadAccountBy(user) } returns account

        // when
        val info = chargeBalanceService.chargeBalance(command)

        // then
        verify { loadUserPort.loadUserBy(1L) }
        verify { loadAccountPort.loadAccountBy(user) }
        verify { updateAccountPort.updateAccount(account) }

        println(info)
        assert(info.balance == BigDecimal.valueOf(11000))
    }
}