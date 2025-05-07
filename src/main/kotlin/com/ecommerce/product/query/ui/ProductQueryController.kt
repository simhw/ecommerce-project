package com.ecommerce.product.query.ui

import com.ecommerce.product.query.application.ProductQueryService
import com.ecommerce.product.query.application.ProductView
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/products")
class ProductQueryController(
    private val productQueryService: ProductQueryService
) {
    @GetMapping
    fun searchProducts(pageable: Pageable): SearchProductResponse {
        val result = productQueryService.searchProduct(pageable)
        return SearchProductResponse(result)
    }

    @GetMapping("/best")
    fun searchTopSellerProducts(pageable: Pageable): SearchProductResponse {
        val result = productQueryService.searchTopSellerProduct(pageable)
        return SearchProductResponse(result)
    }
}

class SearchProductResponse(
    val products: List<ProductView?>
)