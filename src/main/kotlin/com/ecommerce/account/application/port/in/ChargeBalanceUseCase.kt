package com.ecommerce.account.application.port.`in`

interface ChargeBalanceUseCase {
    fun chargeBalance(command: ChargeBalanceCommand): AccountInfo
}
