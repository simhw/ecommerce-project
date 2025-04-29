package com.ecommerce.usercoupon.command.adapter.out.persistence

import com.ecommerce.coupon.command.adapter.out.persistence.CouponEntityMapper
import com.ecommerce.usercoupon.command.domain.model.UserCoupon
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    uses = [CouponEntityMapper::class],
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface UserCouponEntityMapper {
    fun toUserCouponEntity(userCoupon: UserCoupon): UserCouponEntity

    fun toUserCoupon(userCouponEntity: UserCouponEntity): UserCoupon
}