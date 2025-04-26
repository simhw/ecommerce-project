package com.ecommerce.coupon.adapter.out.persistence.entity;

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
}
