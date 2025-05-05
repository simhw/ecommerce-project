package com.ecommerce.order.query.application

import com.ecommerce.order.command.domain.model.OrderStatus
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime


@Service
class OrderQueryService(
    private val queryFactory: JPAQueryFactory
) {
    val order: QOrderData = QOrderData.orderData
    val item: QOrderLineItemData = QOrderLineItemData.orderLineItemData

    fun searchTopSellers(criteria: SearchTopSellerCriteria): MutableList<TopSellerView>? {
        return queryFactory
            .select(
                Projections.constructor(
                    TopSellerView::class.java,
                    item.productId,
                    item.quantity.sum()
                )
            )
            .from(order)
            .leftJoin(item)
            .on(order.id.eq(item.order.id))
            .where(
                betweenCreatedDate(criteria.startDate, criteria.endDate),
                hasStatus(OrderStatus.ORDERED, OrderStatus.PAID)
            )
            .groupBy(item.productId)
            .orderBy(item.quantity.sum().desc())
            .limit(30)
            .fetch()
    }

    private fun betweenCreatedDate(startDate: LocalDate, endDate: LocalDate): BooleanExpression {
        return order.createdAt.between(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX))
    }

    private fun equalStatus(status: OrderStatus): BooleanExpression {
        return order.status.eq(status)
    }

    private fun hasStatus(vararg statuses: OrderStatus): BooleanExpression {
        return order.status.`in`(*statuses)
    }
}