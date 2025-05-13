package com.ecommerce.order.command.adapter.out.persistence

import com.ecommerce.order.command.adapter.out.persistence.entity.OrderEntity
import com.ecommerce.order.command.adapter.out.persistence.entity.OrderLineItemEntity
import com.ecommerce.order.command.domain.model.Order
import com.ecommerce.order.command.domain.model.OrderLineItem
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(
    componentModel = "spring",
    uses = [OrderLineItemEntityMapper::class]
)
interface OrderEntityMapper {
    fun toOrderEntity(order: Order): OrderEntity

    @Mapping(target = "items", ignore = true)
    fun toOrder(orderEntity: OrderEntity): Order
}

@Mapper(componentModel = "spring")
interface OrderLineItemEntityMapper {
    @Mapping(target = "order", ignore = true)
    fun toOrderLineItemEntity(orderLineItem: OrderLineItem): OrderLineItemEntity
}