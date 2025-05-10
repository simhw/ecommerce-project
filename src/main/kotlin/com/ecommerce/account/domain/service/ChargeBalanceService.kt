package com.ecommerce.account.domain.service

import com.ecommerce.account.application.port.`in`.AccountInfo
import com.ecommerce.account.application.port.`in`.ChargeBalanceCommand
import com.ecommerce.account.application.port.`in`.ChargeBalanceUseCase
import com.ecommerce.account.application.port.out.LoadAccountPort
import com.ecommerce.account.application.port.out.SaveAccountPort
import com.ecommerce.common.model.Money
import com.ecommerce.user.application.out.LoadUserPort
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ChargeBalanceService(
    val loadUserPort: LoadUserPort,
    val loadAccountPort: LoadAccountPort,
    val saveAccountPort: SaveAccountPort
) : ChargeBalanceUseCase {
    @Transactional
    override fun chargeBalance(command: ChargeBalanceCommand): AccountInfo {
        val user = loadUserPort.loadUserBy(command.userId)
        user.verifyActiveUser()

        val account = loadAccountPort.loadAccountBy(user)
        account.deposit(Money(command.amount))

        saveAccountPort.saveAccount(account)
        return AccountInfo.from(account)
    }
}