package com.ecommerce.account.domain.model

import com.ecommerce.common.model.Money

class Account(
    val id: Long,
    private var balance: Money
) {
    fun deposit(amount: Money) {
        this.balance.plus(amount)
    }

    fun withdraw(amount: Money) {
        this.balance.minus(amount)
    }
}