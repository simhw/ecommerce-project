package com.ecommerce.user.domain.model

import java.time.LocalDateTime

class User(
    val id: Long? = null,
    var email: String,
    var name: String?,
    val createdAt: LocalDateTime,
    var deletedAt: LocalDateTime? = null
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

    fun getIdOrThrow() = id ?: throw IllegalStateException("id is null.")

    fun verifyActiveUser() {
        deletedAt ?: throw IllegalStateException("invalid active user.")
    }

    fun withdraw() {
        this.deletedAt = LocalDateTime.now()
    }
}
