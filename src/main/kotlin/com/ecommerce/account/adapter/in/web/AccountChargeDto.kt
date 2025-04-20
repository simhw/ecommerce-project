package com.ecommerce.account.adapter.`in`.web

object AccountChargeDto {
    data class Request(
        val amount: Long
    )

    data class Response(
        val account: AccountDto
    )
}
