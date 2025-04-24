package com.ecommerce.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QuerydslConfig (
    @PersistenceContext val entityManager: EntityManager
) {
    @Bean
    fun jpqQueryFactory() = JPAQueryFactory(entityManager)
}