package com.ecommerce.user.domain.model

import java.time.LocalDateTime

class User(
    val id: Long,
    val email: String,
    val name: String,
    val createdAt: LocalDateTime?,
    private val deletedAt: LocalDateTime?
) {
    fun verifyActiveUser() {
        if (deletedAt != null) {
            throw IllegalArgumentException("invalid user")
        }
    }
}