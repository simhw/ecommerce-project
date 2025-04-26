package com.ecommerce.usercoupon.command.adapter.out.persistence

import com.ecommerce.coupon.adapter.out.persistence.CouponEntityMapper
import com.ecommerce.usercoupon.command.domain.model.UserCoupon
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    uses = [CouponEntityMapper::class],
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface UserCouponEntityMapper {
    @Mapping(target = "userId", source = "user.id")
    fun toUserCouponEntity(userCoupon: UserCoupon): UserCouponEntity
}