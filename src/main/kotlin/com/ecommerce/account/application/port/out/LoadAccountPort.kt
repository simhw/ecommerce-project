package com.ecommerce.account.application.port.out

import com.ecommerce.account.domain.model.Account
import com.ecommerce.user.domain.model.User

interface LoadAccountPort {
    fun loadAccountBy(user: User): Account
}