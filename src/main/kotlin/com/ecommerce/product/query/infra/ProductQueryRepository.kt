package com.ecommerce.product.query.infra

import com.ecommerce.product.query.application.ProductView
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.redisson.api.RScoredSortedSet
import org.redisson.api.RedissonClient
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class ProductQueryRepository(
    private val queryFactory: JPAQueryFactory,
    private val redissonClient: RedissonClient
) {
    companion object {
        const val TOP_SELLER_KEY = "top:selling:products"
    }

    private val product = QProductData.productData
    private val stock = QProductStockData.productStockData

    fun searchProduct(pageable: Pageable): List<ProductView> {
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

    fun getProduct(id: Long): ProductView? {
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
            .where(product.id.eq(id))
            .fetchOne()
    }

    fun getAllTopSellerProductId(pageable: Pageable): List<Long> {
        val startIndex = pageable.offset.toInt()
        val endIndex = (pageable.offset + pageable.pageSize).toInt() - 1

        val sortedSet: RScoredSortedSet<Long> = redissonClient.getScoredSortedSet(TOP_SELLER_KEY)
        return sortedSet.valueRangeReversed(startIndex, endIndex).toList()
    }
}