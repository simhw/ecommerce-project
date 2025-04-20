package com.ecommerce.account.adapter.`in`.web

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class AccountController(
) {
    @GetMapping("/users/{userId}/account")
    fun getAccount(
        @PathVariable userId: Long,
    ): AccountDto = AccountDto(1L, 1000)

    @PostMapping("/users/{userId}/account/charge")
    fun chargeAccount(
        @PathVariable userId: Long,
        @RequestBody request: AccountChargeDto.Request
    ): AccountChargeDto.Response =
        AccountChargeDto.Response(AccountDto(1, 10000))
}
