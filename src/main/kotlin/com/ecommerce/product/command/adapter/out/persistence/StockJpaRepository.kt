package com.ecommerce.product.command.adapter.out.persistence

import com.ecommerce.product.command.adapter.out.persistence.entity.StockEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import java.util.Optional

interface StockJpaRepository : JpaRepository<StockEntity, Long> {
    fun findByProductId(productId: Long): Optional<StockEntity>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findForUpdateByProductId(productId: Long): Optional<StockEntity>
}