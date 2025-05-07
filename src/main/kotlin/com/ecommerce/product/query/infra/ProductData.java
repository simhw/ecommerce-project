package com.ecommerce.product.query.infra;

import com.ecommerce.common.model.Money;
import com.ecommerce.product.command.domain.model.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "product")
public class ProductData {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price_amount"))
    private Money price;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    protected ProductData() {
    }
}
