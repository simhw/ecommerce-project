package com.ecommerce.order.query.application

import org.redisson.api.RScoredSortedSet
import org.redisson.api.RedissonClient
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate


@Service
class RankingScheduler(
    private val orderQueryService: OrderQueryService,
    private val redissonClient: RedissonClient
) {
    private val TOP_SELLER_KEY = "top:selling:products"

    /**
     * 3일 동안 가장 많이 판매된 상품 집계
     */
    @Scheduled(cron = "0 0 0 * * *")
    fun updateTopSellerRanking() {
        val criteria = SearchTopSellerCriteria(LocalDate.now().minusDays(3), LocalDate.now())
        val topSellers = orderQueryService.searchTopSellers(criteria)
        val zSet: RScoredSortedSet<String> = redissonClient.getScoredSortedSet(TOP_SELLER_KEY)

        // 랭킹 초기화
        zSet.clear()
        topSellers?.map { zSet.add(it.totalQuantity.toDouble(), it.productId.toString()) }
    }
}