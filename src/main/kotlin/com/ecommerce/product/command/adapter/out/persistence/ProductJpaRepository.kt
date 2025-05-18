package com.ecommerce.product.command.adapter.out.persistence

import com.ecommerce.product.command.adapter.out.persistence.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository : JpaRepository<ProductEntity, Long>