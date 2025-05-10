package com.ecommerce.order.query.application;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Entity
@Table(name = "order_line_item")
public class OrderLineItemData {
    @Id
    @Column(name = "order_line_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderData order;

    protected OrderLineItemData() {
    }
}
