package com.ecommerce.product.command.adapter.out.persistence

import com.ecommerce.product.command.adapter.out.persistence.entity.ProductStockEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductStockJpaRepository : JpaRepository<ProductStockEntity, Long>