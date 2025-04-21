package com.ecommerce.account.application.port.`in`

import com.ecommerce.account.domain.model.Account
import java.math.BigDecimal
import java.time.LocalDateTime

data class AccountInfo(
    val id: Long,
    val balance: BigDecimal,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(account: Account) = AccountInfo(
            account.id,
            account.balance.amount,
            account.updatedAt
        )
    }
}