package com.ecommerce.user.adapter.out.persistence

import com.ecommerce.user.domain.model.User
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface UserEntityMapper {
    fun toUserEntity(user: User): UserEntity
    fun toUser(userEntity: UserEntity): User
}