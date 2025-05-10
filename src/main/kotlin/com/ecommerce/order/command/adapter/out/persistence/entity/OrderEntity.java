package com.ecommerce.order.command.adapter.out.persistence.entity;

import com.ecommerce.common.BaseEntity;
import com.ecommerce.common.model.Address;
import com.ecommerce.common.model.Money;
import com.ecommerce.order.command.domain.model.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItemEntity> items = new ArrayList<>();

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "total_amounts"))
    private Money totalAmounts;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "total_discount_amounts"))
    private Money totalDiscountAmounts;

    private OrderStatus status;

    protected OrderEntity() {
    }

    @Builder
    public OrderEntity(LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
                       Long id, String number, Long userId, Address address, List<OrderLineItemEntity> items,
                       Money totalAmounts, Money totalDiscountAmounts, OrderStatus status) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.number = number;
        this.userId = userId;
        this.address = address;
        this.items = items;
        this.totalAmounts = totalAmounts;
        this.totalDiscountAmounts = totalDiscountAmounts;
        this.status = status;
    }

    public void addItem(OrderLineItemEntity item) {
        this.items.add(item);
        item.setOrder(this);
    }

    public void setItems(List<OrderLineItemEntity> items) {
        this.items = items;
        items.forEach(it -> it.setOrder(this));
    }
}
