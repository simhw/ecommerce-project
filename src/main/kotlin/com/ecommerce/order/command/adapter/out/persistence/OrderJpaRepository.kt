package com.ecommerce.order.command.adapter.out.persistence

import com.ecommerce.order.command.adapter.out.persistence.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderJpaRepository : JpaRepository<OrderEntity, Long>