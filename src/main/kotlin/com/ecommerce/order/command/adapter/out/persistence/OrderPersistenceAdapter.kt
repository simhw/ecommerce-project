package com.ecommerce.order.command.adapter.out.persistence

import com.ecommerce.order.command.application.out.SaveOrderPort
import com.ecommerce.order.command.domain.model.Order
import org.springframework.stereotype.Repository

@Repository
class OrderPersistenceAdapter(
    private val orderJpaRepository: OrderJpaRepository,
    private val orderEntityMapper: OrderEntityMapper
) : SaveOrderPort {
    override fun saveOrder(order: Order) {
        val orderEntity = orderEntityMapper.toOrderEntity(order)
        orderJpaRepository.save(orderEntity)
    }
}