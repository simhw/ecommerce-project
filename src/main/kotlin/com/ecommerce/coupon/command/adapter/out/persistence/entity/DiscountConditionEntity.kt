package com.ecommerce.coupon.command.adapter.out.persistence.entity

import com.ecommerce.common.model.Money
import com.ecommerce.common.model.Period
import jakarta.persistence.*
import jakarta.persistence.CascadeType.*
import jakarta.persistence.FetchType.*

@Entity
@Table(name = "discount_condition")
@DiscriminatorColumn(name = "condition_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class DiscountConditionEntity(
    @Id
    @Column(name = "condition_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    var parent: DiscountConditionEntity?,

    @OneToMany(mappedBy = "parent", fetch = LAZY, cascade = [ALL], orphanRemoval = true)
    var children: MutableList<DiscountConditionEntity>? = mutableListOf()
) {
    fun add(child: DiscountConditionEntity) {
        child.parent = this
        this.children?.add(child)
    }
}

/**
 * and 조건
 */
@Entity
@DiscriminatorValue("all")
class AllConditionEntity(
    id: Long? = null,
    parent: DiscountConditionEntity? = null,
    children: MutableList<DiscountConditionEntity> = mutableListOf()
) : DiscountConditionEntity(id, parent, children)

/**
 * or 조건
 */
@Entity
@DiscriminatorValue("any")
class AnyConditionEntity(
    id: Long? = null,
    parent: DiscountConditionEntity? = null,
    children: MutableList<DiscountConditionEntity> = mutableListOf()
) : DiscountConditionEntity(id, parent, children)

/**
 * 기간 조건
 */
@Entity
@DiscriminatorValue("period")
class PeriodConditionEntity(
    @Embedded
    val period: Period,
    id: Long? = null,
    parent: DiscountConditionEntity? = null,
    children: MutableList<DiscountConditionEntity>? = null,
) : DiscountConditionEntity(id, parent, children)

/***
 * 금액 조건
 */
@Entity
@DiscriminatorValue("amount")
class AmountConditionEntity(
    @AttributeOverride(name = "amount", column = Column(name = "amount_condition"))
    val amount: Money,
    id: Long? = null,
    parent: DiscountConditionEntity? = null,
    children: MutableList<DiscountConditionEntity>? = null,
) : DiscountConditionEntity(id, parent, children)

/**
 * 무조건
 */
@Entity
@DiscriminatorValue("none")
class NoneConditionEntity(
    id: Long? = null,
    parent: DiscountConditionEntity? = null,
    children: MutableList<DiscountConditionEntity>? = null
) : DiscountConditionEntity(id, parent, children)