package com.ecommerce.usercoupon.query.application;

import com.ecommerce.coupon.query.application.CouponData;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user_coupon")
public class UserCouponData {
    @Id
    @Column(name = "user_coupon_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "coupon_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private CouponData coupon;

    private LocalDateTime createdAt;

    protected UserCouponData() {
    }
}
