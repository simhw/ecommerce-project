package com.ecommerce.order.command.adapter.out.persistence

import com.ecommerce.order.command.application.out.LoadOrderPort
import com.ecommerce.order.command.application.out.SaveOrderPort
import com.ecommerce.order.command.domain.model.Order
import org.springframework.stereotype.Repository

@Repository
class OrderPersistenceAdapter(
    private val orderJpaRepository: OrderJpaRepository,
    private val orderEntityMapper: OrderEntityMapper
) : SaveOrderPort, LoadOrderPort {
    override fun saveOrder(order: Order) {
        val orderEntity = orderEntityMapper.toOrderEntity(order)
        orderJpaRepository.save(orderEntity)
    }

    override fun loadOrderBy(id: Long): Order {
        val orderEntity = orderJpaRepository.findById(id)
            .orElseThrow { IllegalArgumentException("not found order by id: $id") }
        return orderEntityMapper.toOrder(orderEntity)
    }

    override fun loadOrderBy(number: String): Order {
        val orderEntity = orderJpaRepository.findByNumber(number)
        return orderEntityMapper.toOrder(orderEntity)
    }
}