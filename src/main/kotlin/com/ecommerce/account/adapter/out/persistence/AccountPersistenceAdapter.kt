package com.ecommerce.account.adapter.out.persistence

import com.ecommerce.account.application.port.out.LoadAccountPort
import com.ecommerce.account.application.port.out.SaveAccountPort
import com.ecommerce.account.domain.model.Account
import com.ecommerce.user.domain.model.User
import org.springframework.stereotype.Repository

@Repository
class AccountPersistenceAdapter(
    private val accountJpaRepository: AccountJpaRepository,
    private val accountEntityMapper: AccountEntityMapper
) : LoadAccountPort, SaveAccountPort {
    override fun loadAccountBy(user: User): Account {
        val accountEntity = accountJpaRepository.findByUserId(user.getIdOrThrow())
            .orElseThrow { IllegalArgumentException("not found account") }
        return accountEntityMapper.toAccount(accountEntity)
    }

    override fun saveAccount(account: Account) {
        val accountEntity = accountEntityMapper.toAccountEntity(account)
        accountJpaRepository.save(accountEntity)
    }
}