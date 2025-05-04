package com.ecommerce.coupon.command.adapter.out.persistence.entity;

import com.ecommerce.common.model.DateTimePeriod;
import com.ecommerce.common.model.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Entity
@DiscriminatorValue("AMOUNT_DISCOUNT")
public class AmountDiscountCouponEntity extends CouponEntity {
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "discount_amount"))
    private Money amount;

    protected AmountDiscountCouponEntity() {
    }

    @Builder
    public AmountDiscountCouponEntity(
            Long id,
            String name,
            String description,
            Money minOrderAmount,
            Money maxDiscountAmount,
            DateTimePeriod issueOfPeriod,
            DateTimePeriod useOfPeriod,
            Money amount
    ) {
        super(id, name, description, minOrderAmount, maxDiscountAmount, issueOfPeriod, useOfPeriod);
        this.amount = amount;
    }
}
