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
    @Mapping(source = "user.id", target = "userId")
    fun toOrderEntity(order: Order): OrderEntity
}

@Mapper(componentModel = "spring")
interface OrderLineItemEntityMapper {
    @Mapping(target = "order", ignore = true)
    @Mapping(source = "product.id", target = "productId")
    fun toOrderLineItemEntity(orderLineItem: OrderLineItem): OrderLineItemEntity
}