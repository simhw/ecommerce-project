package com.ecommerce.coupon.command.adapter.out.persistence

import com.ecommerce.coupon.command.adapter.out.persistence.entity.AmountDiscountCouponEntity
import com.ecommerce.coupon.command.adapter.out.persistence.entity.CouponEntity
import com.ecommerce.coupon.command.adapter.out.persistence.entity.PercentDiscountCouponEntity
import com.ecommerce.coupon.domain.model.AmountDiscountCoupon
import com.ecommerce.coupon.domain.model.Coupon
import com.ecommerce.coupon.domain.model.PercentDiscountCoupon
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.mapstruct.SubclassExhaustiveStrategy
import org.mapstruct.SubclassMapping

@Mapper(
    componentModel = "spring",
    subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface CouponEntityMapper {
    @SubclassMapping(source = PercentDiscountCouponEntity::class, target = PercentDiscountCoupon::class)
    @SubclassMapping(source = AmountDiscountCouponEntity::class, target = AmountDiscountCoupon::class)
    fun toCoupon(couponEntity: CouponEntity): Coupon

    @SubclassMapping(source = PercentDiscountCoupon::class, target = PercentDiscountCouponEntity::class)
    @SubclassMapping(source = AmountDiscountCoupon::class, target = AmountDiscountCouponEntity::class)
    fun toCouponEntity(coupon: Coupon): CouponEntity
}