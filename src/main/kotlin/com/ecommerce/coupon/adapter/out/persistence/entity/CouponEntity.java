package com.ecommerce.coupon.adapter.out.persistence.entity;

import com.ecommerce.common.BaseEntity;
import com.ecommerce.common.model.DateTimePeriod;
import com.ecommerce.common.model.Money;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "coupon")
@DiscriminatorColumn(name = "discount_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class CouponEntity extends BaseEntity {
    @Id
    @Column(name = "coupon_id")
    private Long id;
    private String name;
    private String description;

    // 최소 주문 금액
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "min_order_amount"))
    private Money minOrderAmount;

    // 최대 할인 금액
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "max_discount_amount"))
    private Money maxDiscountAmount;

    // 발행 기간
    @Embedded
    @AttributeOverride(name = "startDateTime", column = @Column(name = "issue_start_at"))
    @AttributeOverride(name = "endDateTime", column = @Column(name = "issue_end_at"))
    private DateTimePeriod issueOfPeriod;

    // 사용 기간
    @Embedded
    @AttributeOverride(name = "startDateTime", column = @Column(name = "use_start_at"))
    @AttributeOverride(name = "endDateTime", column = @Column(name = "use_end_at"))
    private DateTimePeriod useOfPeriod;

    protected CouponEntity() {
    }
}
