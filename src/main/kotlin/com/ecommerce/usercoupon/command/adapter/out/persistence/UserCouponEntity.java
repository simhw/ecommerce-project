package com.ecommerce.usercoupon.command.adapter.out.persistence;

import com.ecommerce.common.BaseEntity;
import com.ecommerce.coupon.command.adapter.out.persistence.entity.CouponEntity;
import com.ecommerce.usercoupon.command.domain.model.UserCouponStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@Entity
@Table(name = "user_coupon")
public class UserCouponEntity extends BaseEntity {
    @Id
    @Column(name = "user_coupon_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private UserCouponStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private CouponEntity coupon;

    protected UserCouponEntity() {
    }
}
