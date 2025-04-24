package com.ecommerce.user.domain.model

import java.time.LocalDateTime

class User(
    val id: Long?,
    var email: String,
    var name: String?,
    val createdAt: LocalDateTime,
    var deletedAt: LocalDateTime?
) {
    companion object {
        fun register(email: String, name: String?): User {
            return User(
                id = null,
                email = email,
                name = name,
                createdAt = LocalDateTime.now(),
                deletedAt = null
            )
        }
    }

    fun verifyActiveUser() {
        deletedAt?.let { throw IllegalArgumentException("invalid user") }
    }

    fun withdraw() {
        this.deletedAt = LocalDateTime.now()
    }
}
