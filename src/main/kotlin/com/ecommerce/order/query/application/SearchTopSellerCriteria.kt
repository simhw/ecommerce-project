package com.ecommerce.order.query.application

import java.time.LocalDate

data class SearchTopSellerCriteria (
    val startDate: LocalDate,
    val endDate: LocalDate,
)