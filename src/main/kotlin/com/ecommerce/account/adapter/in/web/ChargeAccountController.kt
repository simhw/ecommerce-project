package com.ecommerce.account.adapter.`in`.web

import com.ecommerce.account.application.port.`in`.AccountInfo
import com.ecommerce.account.application.port.`in`.ChargeBalanceCommand
import com.ecommerce.account.application.port.`in`.ChargeBalanceUseCase
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/api/v1")
class ChargeAccountController(
    val chargeBalanceUseCase: ChargeBalanceUseCase
) {
    @PostMapping("/users/{userId}/account/charge")
    fun chargeAccount(
        @PathVariable userId: Long,
        @RequestBody request: AccountChargeRequest
    ): AccountChargeResponse {
        val command = AccountChargeRequest.of(userId, request)
        val info = chargeBalanceUseCase.chargeBalance(command)
        return AccountChargeResponse.from(info)
    }
}

data class AccountChargeRequest(
    val amount: Long
) {
    companion object {
        fun of(
            userId: Long,
            request: AccountChargeRequest
        ) = ChargeBalanceCommand(
            userId = userId,
            amount = BigDecimal.valueOf(request.amount)
        )
    }
}

data class AccountChargeResponse(
    val account: AccountDto
) {
    companion object {
        fun from(
            info: AccountInfo
        ) = AccountChargeResponse(
            AccountDto(
                balance = info.balance.longValueExact(),
                updatedAt = info.updatedAt
            )
        )
    }
}