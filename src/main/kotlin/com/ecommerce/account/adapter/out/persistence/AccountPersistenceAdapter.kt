package com.ecommerce.account.adapter.out.persistence

import com.ecommerce.account.application.port.out.LoadAccountPort
import com.ecommerce.account.application.port.out.UpdateAccountPort
import com.ecommerce.account.domain.model.Account
import com.ecommerce.user.domain.model.User
import org.springframework.stereotype.Repository

@Repository
class AccountPersistenceAdapter: LoadAccountPort, UpdateAccountPort {
    override fun loadAccountBy(user: User): Account {
        TODO("Not yet implemented")
    }

    override fun updateAccount(account: Account) {
        TODO("Not yet implemented")
    }
}