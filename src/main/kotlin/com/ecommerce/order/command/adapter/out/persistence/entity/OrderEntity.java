package com.ecommerce.order.command.adapter.out.persistence.entity;

import com.ecommerce.common.BaseEntity;
import com.ecommerce.common.model.Address;
import com.ecommerce.common.model.Money;
import com.ecommerce.order.command.domain.model.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number")
    private String number;

    private Long userId;

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

    private OrderStatus status;

    protected OrderEntity() {
    }
}
