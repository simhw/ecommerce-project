package com.ecommerce.coupon.command.adapter.out.persistence

import com.ecommerce.coupon.command.adapter.out.persistence.entity.*
import com.ecommerce.coupon.command.domain.*
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    uses = [DiscountConditionEntityMapper::class],
    subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface CouponEntityMapper {
    @SubclassMapping(source = PercentDiscountCouponEntity::class, target = PercentDiscountCoupon::class)
    @SubclassMapping(source = AmountDiscountCouponEntity::class, target = AmountDiscountCoupon::class)
    fun to(entity: CouponEntity): Coupon

    @SubclassMapping(source = PercentDiscountCoupon::class, target = PercentDiscountCouponEntity::class)
    @SubclassMapping(source = AmountDiscountCoupon::class, target = AmountDiscountCouponEntity::class)
    fun to(coupon: Coupon): CouponEntity
}

@Mapper(componentModel = "spring")
abstract class DiscountConditionEntityMapper {
    fun map(entity: DiscountConditionEntity): DiscountCondition {
        return when (entity) {
            is AmountConditionEntity -> AmountCondition(entity.amount)
            is PeriodConditionEntity -> PeriodCondition(entity.period)
            is NoneConditionEntity -> NoneCondition()

            // recursion
            is AllConditionEntity -> AllCondition(entity.children?.map { map(it) }
                ?: throw IllegalArgumentException("${entity::class.simpleName} hasn't sub condition"))

            is AnyConditionEntity -> AnyCondition(entity.children?.map { map(it) }
                ?: throw IllegalArgumentException("${entity::class.simpleName} hasn't sub condition"))

            else -> throw IllegalArgumentException("Unknown DiscountConditionEntity: ${entity::class}")
        }
    }

    fun map(condition: DiscountCondition): DiscountConditionEntity {
        return map(condition, null)
    }

    private fun map(condition: DiscountCondition, parent: DiscountConditionEntity?): DiscountConditionEntity {
        val entity = when (condition) {
            // leaf condition
            is AmountCondition -> AmountConditionEntity(condition.amount)
            is PeriodCondition -> PeriodConditionEntity(condition.period)
            is NoneCondition -> NoneConditionEntity()

            // root condition
            is AllCondition -> {
                val entity = AllConditionEntity(null, parent, mutableListOf())
                for (child in condition.conditions) {
                    entity.add(map(child, entity))
                }

                return entity
            }

            is AnyCondition -> {
                val entity = AnyConditionEntity(null, parent, mutableListOf())
                for (child in condition.conditions) {
                    entity.add(map(child, entity))
                }

                return entity
            }
        }
        return entity
    }
}



