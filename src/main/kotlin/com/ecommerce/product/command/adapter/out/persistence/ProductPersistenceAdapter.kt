package com.ecommerce.product.command.adapter.out.persistence

import com.ecommerce.product.command.application.out.LoadProductPort
import com.ecommerce.product.command.application.out.SaveProductPort
import com.ecommerce.product.command.domain.model.Product
import com.ecommerce.product.command.domain.model.ProductStock
import org.springframework.stereotype.Repository

@Repository
class ProductPersistenceAdapter(
    private val productJpaRepository: ProductJpaRepository,
    private val productStockJapRepository: ProductStockJpaRepository,
    private val productEntityMapper: ProductEntityMapper,
    private val productStockEntityMapper: ProductStockEntityMapper
) : LoadProductPort, SaveProductPort {
    override fun loadProductBy(id: Long): Product {
        val productEntity = productJpaRepository.findById(id)
            .orElseThrow { IllegalArgumentException("not found product") }
        return productEntityMapper.toProduct(productEntity)
    }

    override fun saveProduct(product: Product) {
        val productEntity = productEntityMapper.toProductEntity(product)
        productJpaRepository.save(productEntity)
    }

    override fun saveStock(stock: ProductStock) {
        val productStockEntity = productStockEntityMapper.toProductStockEntity(stock)
        productStockJapRepository.save(productStockEntity)
    }
}