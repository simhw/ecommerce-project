package com.ecommerce.product.command.adapter.out.persistence

import com.ecommerce.product.command.application.out.LoadProductPort
import com.ecommerce.product.command.domain.model.Product
import org.springframework.stereotype.Repository

@Repository
class ProductPersistenceAdapter(
    private val productJpaRepository: ProductJpaRepository,
    private val productEntityMapper: ProductEntityMapper
) : LoadProductPort {
    override fun loadProductBy(id: Long): Product {
        val productEntity = productJpaRepository.findById(id)
            .orElseThrow { IllegalArgumentException("not found product") }
        return productEntityMapper.toProduct(productEntity)
    }
}