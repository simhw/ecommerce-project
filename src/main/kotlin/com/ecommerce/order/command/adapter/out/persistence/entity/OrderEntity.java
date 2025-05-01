package com.ecommerce.order.command.adapter.out.persistence.entity;

import com.ecommerce.common.model.Address;
import com.ecommerce.common.model.Money;
import com.ecommerce.order.command.domain.model.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;

import java.util.List;

@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @Column(name = "order_id")
    private Long id;

    private Long userId;

    private OrderStatus status;

    @Embedded
    @AttributeOverride(name = "street", column = @Column(name = "shipping_street"))
    @AttributeOverride(name = "city", column = @Column(name = "shipping_city"))
    private Address address;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItemEntity> items;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "total_amounts"))
    private Money totalAmounts;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "total_discount_amounts"))
    private Money totalDiscountAmounts;

    protected OrderEntity() {
    }
}
