package com.ecommerce.user.application.out

import com.ecommerce.user.domain.model.User

interface LoadUserPort {
    fun loadUserBy(id: Long): User
    fun loadUserBy(email: String): User
}