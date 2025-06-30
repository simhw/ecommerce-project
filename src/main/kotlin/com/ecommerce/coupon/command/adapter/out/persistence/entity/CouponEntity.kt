package com.ecommerce.coupon.command.adapter.out.persistence.entity

import com.ecommerce.common.BaseEntity
import com.ecommerce.common.model.Money
import com.ecommerce.common.model.Period
import jakarta.persistence.*
import jakarta.persistence.CascadeType.*
import jakarta.persistence.FetchType.*
import lombok.AllArgsConstructor
import java.math.BigDecimal

@AllArgsConstructor
@Entity
@Table(name = "coupon")
@DiscriminatorColumn(name = "discount_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class CouponEntity(
    @Id
    @Column(name = "coupon_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val description: String,

    @Embedded
    val period: Period,

    @OneToOne(fetch = LAZY, cascade = [ALL], orphanRemoval = true)
    @JoinColumn(name = "coupon_id")
    val condition: DiscountConditionEntity
) : BaseEntity()

@Entity
@DiscriminatorValue("percent")
class PercentDiscountCouponEntity(
    id: Long? = null,
    name: String,
    description: String,
    period: Period,
    condition: DiscountConditionEntity,
    @Column(name = "discount_percent")
    val percent: BigDecimal,
    val maxDiscountAmount: Money
) : CouponEntity(id, name, description, period, condition)

@Entity
@DiscriminatorValue("amount")
class AmountDiscountCouponEntity(
    id: Long? = null,
    name: String,
    description: String,
    period: Period,
    condition: DiscountConditionEntity,
    @AttributeOverride(name = "amount", column = Column(name = "discount_amount"))
    val amount: Money
) : CouponEntity(id, name, description, period, condition)

