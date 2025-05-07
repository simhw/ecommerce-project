package com.ecommerce.product.query.application

import com.ecommerce.product.query.infra.ProductQueryRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductQueryService(
    private val productQueryRepository: ProductQueryRepository
) {
    fun searchProduct(pageable: Pageable): List<ProductView> {
        return productQueryRepository.searchProduct(pageable)
    }

    fun searchTopSellerProduct(pageable: Pageable): List<ProductView?> {
        val ids = productQueryRepository.getAllTopSellerProductId(pageable)
        return ids.map { productQueryRepository.getProduct(it) }
    }
}