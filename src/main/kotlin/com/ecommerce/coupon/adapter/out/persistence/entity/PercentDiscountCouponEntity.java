package com.ecommerce.coupon.adapter.out.persistence.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@Entity
@DiscriminatorValue("PERCENT_DISCOUNT")
public class PercentDiscountCouponEntity extends CouponEntity {
    private BigDecimal percent;

    protected PercentDiscountCouponEntity() {
    }
}
