package com.ecommerce.account.application.port.out

import com.ecommerce.account.domain.model.Account

interface SaveAccountPort {
    fun saveAccount(account: Account)
}