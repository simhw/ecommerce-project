package com.ecommerce.account.domain.model

import com.ecommerce.common.model.Money
import com.ecommerce.user.domain.model.User
import java.time.LocalDateTime

class Account(
    val id: Long,
    var balance: Money,
    val user: User,
    val deletedAt: LocalDateTime?,
    val updatedAt: LocalDateTime
) {
    fun deposit(amount: Money) {
        balance = this.balance.plus(amount)
    }

    fun withdraw(amount: Money) {
        balance = this.balance.minus(amount)
    }
}