package com.ecommerce.coupon.command.adapter.out.persistence.entity;

import com.ecommerce.common.model.DateTimePeriod;
import com.ecommerce.common.model.Money;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Entity
@DiscriminatorValue("PERCENT_DISCOUNT")
public class PercentDiscountCouponEntity extends CouponEntity {
    private BigDecimal percent;

    protected PercentDiscountCouponEntity() {
    }

    @Builder
    public PercentDiscountCouponEntity(
            Long id,
            String name,
            String description,
            Money minOrderAmount,
            Money maxDiscountAmount,
            DateTimePeriod issueOfPeriod,
            DateTimePeriod useOfPeriod,
            BigDecimal percent
    ) {
        super(id, name, description, minOrderAmount, maxDiscountAmount, issueOfPeriod, useOfPeriod);
        this.percent = percent;
    }
}
