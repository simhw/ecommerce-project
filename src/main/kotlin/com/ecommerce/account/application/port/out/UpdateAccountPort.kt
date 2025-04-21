package com.ecommerce.account.application.port.out

import com.ecommerce.account.domain.model.Account

interface UpdateAccountPort {
    fun updateAccount(account: Account)
}