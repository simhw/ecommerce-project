package com.ecommerce.account.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AccountJpaRepository : JpaRepository<AccountEntity, Long> {
    fun findByUserId(userId: Long): Optional<AccountEntity>
}