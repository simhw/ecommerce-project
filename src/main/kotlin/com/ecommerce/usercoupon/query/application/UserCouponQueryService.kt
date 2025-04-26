package com.ecommerce.usercoupon.query.application

import com.ecommerce.coupon.query.application.QCouponData
import com.ecommerce.coupon.query.ui.CouponSummary
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Service


@Service
class UserCouponQueryService(
    private val queryFactory: JPAQueryFactory
) {
    fun searchUserCouponsBy(userId: Long): List<UserCouponView>? {
        val userCoupon = QUserCouponData.userCouponData
        val coupon = QCouponData.couponData

        return queryFactory
            .select(
                Projections.constructor(
                    UserCouponView::class.java,
                    Projections.constructor(
                        CouponSummary::class.java,
                        coupon.id,
                        coupon.name,
                        coupon.description,
                        coupon.discountType,
                        coupon.percent,
                        coupon.amount.amount
                    ),
                    userCoupon.createdAt
                )
            )
            .from(userCoupon)
            .join(coupon)
            .on(userCoupon.coupon.id.eq(coupon.id))
            .where(userCoupon.userId.eq(userId)).fetch()

    }
}