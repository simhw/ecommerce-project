package com.ecommerce.product.adapter.`in`.web


import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/products")
class ProductController {
    @GetMapping
    fun findProducts(): List<ProductDto> = listOf(
        ProductDto(1L, "product1", 10000, 10),
        ProductDto(2L, "product2", 20000, 10),
        ProductDto(3L, "product3", 30000, 10)
    )

    @GetMapping
    @RequestMapping("/best")
    fun findBestProducts(): List<ProductDto> = listOf(
        ProductDto(1L, "product1", 10000, 10),
        ProductDto(2L, "product2", 20000, 10),
        ProductDto(3L, "product3", 30000, 10)
    )
}
