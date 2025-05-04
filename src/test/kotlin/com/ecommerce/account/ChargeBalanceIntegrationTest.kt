package com.ecommerce.account

import com.ecommerce.account.adapter.out.persistence.AccountEntity
import com.ecommerce.account.application.port.`in`.ChargeBalanceCommand
import com.ecommerce.account.domain.service.ChargeBalanceService
import com.ecommerce.common.model.Money
import com.ecommerce.user.adapter.out.persistence.UserEntity
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import kotlin.test.assertEquals


@SpringBootTest
@Transactional
class ChargeBalanceIntegrationTest(
    @Autowired val chargeBalanceService: ChargeBalanceService,
    @Autowired val entityManager: EntityManager
) {
    @BeforeEach
    fun init() {
        val userEntity = UserEntity(null, "email", "username")
        val accountEntity = AccountEntity(null, Money.ZERO, userEntity)
        entityManager.persist(userEntity)
        entityManager.persist(accountEntity)

        entityManager.flush()
        entityManager.clear()
    }

    @Test
    fun `충전 시 잔액이 요청 금액만큼 증가`() {
        // given
        val amount = BigDecimal.valueOf(1000).setScale(2)
        val command = ChargeBalanceCommand(1L, amount)

        // when
        chargeBalanceService.chargeBalance(command)

        // then
        val accountEntity = entityManager.find(AccountEntity::class.java, 1L)
        assertEquals(accountEntity.balance.getAmount(), amount)
    }

    @Test
    fun `충전 시 요청 금액이 0인 경우 ArithmeticException 발생`() {
        // given
        val amount = BigDecimal.valueOf(-1).setScale(2)
        val command = ChargeBalanceCommand(1L, amount)

        // when, then
        assertThrows<ArithmeticException> { chargeBalanceService.chargeBalance(command) }
    }
}