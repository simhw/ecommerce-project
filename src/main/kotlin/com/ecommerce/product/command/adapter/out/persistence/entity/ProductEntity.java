package com.ecommerce.product.command.adapter.out.persistence.entity;

import com.ecommerce.common.BaseEntity;
import com.ecommerce.common.model.Money;
import com.ecommerce.product.command.domain.model.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity extends BaseEntity {
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

    @OneToOne(mappedBy = "product", cascade = CascadeType.PERSIST)
    private ProductStockEntity stock;

    protected ProductEntity() {
    }

    public void setStock(ProductStockEntity stock) {
        this.stock = stock;
        stock.setProduct(this);
    }
}
