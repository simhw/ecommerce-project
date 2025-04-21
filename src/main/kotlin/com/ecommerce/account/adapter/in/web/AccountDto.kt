package com.ecommerce.account.adapter.`in`.web

import java.time.LocalDateTime

data class AccountDto(
    val balance: Long,
    val updatedAt: LocalDateTime
) {
    class Response(
        val account: AccountDto
    )
}