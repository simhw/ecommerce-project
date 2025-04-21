package com.ecommerce.user.adapter.out.persistence

import com.ecommerce.user.application.out.LoadUserPort
import com.ecommerce.user.domain.model.User
import org.springframework.stereotype.Repository

@Repository
class UserPersistenceAdapter : LoadUserPort {
    override fun loadUserBy(id: Long): User {
        TODO("Not yet implemented")
    }

    override fun loadUserBy(email: String): User {
        TODO("Not yet implemented")
    }
}