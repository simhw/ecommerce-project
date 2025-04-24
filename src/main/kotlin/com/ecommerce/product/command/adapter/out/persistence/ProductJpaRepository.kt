package com.ecommerce.product.command.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository : JpaRepository<ProductEntity, Long> {
}