package com.ecommerce.product.command.adapter.out.persistence

import com.ecommerce.product.command.application.out.ProductPort
import com.ecommerce.product.command.domain.model.Product
import org.springframework.stereotype.Repository

@Repository
class ProductPersistenceAdapter(
    private val productJpaRepository: ProductJpaRepository,
    private val productEntityMapper: ProductEntityMapper,
) : ProductPort {
    override fun loadProductBy(id: Long): Product {
        val productEntity = productJpaRepository.findById(id)
            .orElseThrow { IllegalArgumentException("not found product") }
        return productEntityMapper.toProduct(productEntity)
    }

    override fun saveProduct(product: Product) {
        val productEntity = productEntityMapper.toProductEntity(product)
        productJpaRepository.save(productEntity)
    }
}