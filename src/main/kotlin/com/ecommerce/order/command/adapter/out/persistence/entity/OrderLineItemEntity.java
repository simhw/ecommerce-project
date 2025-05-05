package com.ecommerce.order.command.adapter.out.persistence.entity;

import com.ecommerce.common.model.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "order_line_item")
public class OrderLineItemEntity {
    @Id
    @Column(name = "order_line_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "item_price"))
    private Money price;

    private BigDecimal quantity;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "item_amount"))
    private Money amount;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    protected OrderLineItemEntity() {
    }

}
