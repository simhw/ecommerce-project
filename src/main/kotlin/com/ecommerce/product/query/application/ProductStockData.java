package com.ecommerce.product.query.application;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_stock")
public class ProductStockData {
    @Id
    @Column(name = "product_stock_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_value")
    private BigDecimal value;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProductData product;

    protected ProductStockData() {
    }
}
