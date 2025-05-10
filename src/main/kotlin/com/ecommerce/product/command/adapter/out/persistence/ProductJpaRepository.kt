package com.ecommerce.product.command.adapter.out.persistence

import com.ecommerce.product.command.adapter.out.persistence.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ProductJpaRepository : JpaRepository<ProductEntity, Long> {
    @Query("select p from ProductEntity p join fetch p.stock where p.id = :id")
    override fun findById(id: Long): Optional<ProductEntity>
}