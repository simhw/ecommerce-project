package com.ecommerce.user.domain.model

import com.ecommerce.account.domain.model.Account

class User(
    id: Long,
    email: String,
    name: String,
    account: Account
) {
}