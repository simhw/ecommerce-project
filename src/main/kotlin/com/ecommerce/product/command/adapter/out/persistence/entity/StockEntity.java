package com.ecommerce.product.command.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@Entity
@Table(name = "stock")
public class StockEntity {
    @Id
    @Column(name = "stock_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_value")
    private BigDecimal value;

    private Long productId;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    protected StockEntity() {
    }
}
