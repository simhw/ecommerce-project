package com.ecommerce.coupon.query.application;

import com.ecommerce.common.model.Money;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "coupon")
public class CouponData {
    @Id
    @Column(name = "coupon_id")
    private Long id;
    private String name;
    private String description;
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "max_discount_amount"))
    private Money maxDiscountAmount;
    @Column(name = "discount_type")
    private String discountType;
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "discount_amount"))
    private Money amount;
    private BigDecimal percent;

    protected CouponData() {
    }
}
