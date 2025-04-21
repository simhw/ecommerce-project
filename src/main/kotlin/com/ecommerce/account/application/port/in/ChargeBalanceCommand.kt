package com.ecommerce.account.application.port.`in`

import java.math.BigDecimal

data class ChargeBalanceCommand(
    val userId: Long,
    val amount: BigDecimal
)