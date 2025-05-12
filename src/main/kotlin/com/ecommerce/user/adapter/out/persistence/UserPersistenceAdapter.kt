package com.ecommerce.user.adapter.out.persistence

import com.ecommerce.user.application.out.LoadUserPort
import com.ecommerce.user.domain.model.User
import org.springframework.stereotype.Repository

@Repository
class UserPersistenceAdapter(
    private val userJpaRepository: UserJpaRepository,
    private val userEntityMapper: UserEntityMapper
) : LoadUserPort {
    override fun loadUserBy(id: Long): User {
        val userEntity = userJpaRepository.findById(id)
            .orElseThrow { IllegalArgumentException("not found user id: $id") }
        return userEntityMapper.toUser(userEntity)
    }

    override fun loadUserBy(email: String): User {
        val userEntity = userJpaRepository.findByEmail(email)
            .orElseThrow { IllegalArgumentException("not found user email: $email") }
        return userEntityMapper.toUser(userEntity)
    }
}