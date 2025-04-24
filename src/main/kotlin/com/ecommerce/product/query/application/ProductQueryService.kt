package com.ecommerce.product.query.application

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductQueryService(
    private val queryFactory: JPAQueryFactory,
) {
    fun searchProduct(pageable: Pageable): List<ProductView> {
        val product = QProductData.productData
        val stock = QProductStockData.productStockData

        return queryFactory
            .select(
                Projections.constructor(
                    ProductView::class.java,
                    product.id,
                    product.name,
                    product.description,
                    product.price.amount,
                    product.status.stringValue(),
                    stock.value.longValue()
                )
            )
            .from(product)
            .join(stock)
            .on(product.id.eq(stock.product.id))
            .offset(pageable.offset)  // 시작 위치
            .limit(pageable.pageSize.toLong())  // 한 페이지 크기
            .fetch()
    }
}