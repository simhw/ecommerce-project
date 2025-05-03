package com.ecommerce.account.adapter.out.persistence

import com.ecommerce.account.domain.model.Account
import com.ecommerce.user.adapter.out.persistence.UserEntityMapper
import org.mapstruct.Mapper

@Mapper(
    componentModel = "spring",
    uses = [UserEntityMapper::class]
)
interface AccountEntityMapper {
    fun toAccount(accountEntity: AccountEntity): Account

    fun toAccountEntity(account: Account): AccountEntity
}