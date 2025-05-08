package com.ecommerce.common.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NumberUtils {
    companion object {

        /**
         * 주문 번호 생성
         */
        fun generateOrderNumber(): String {
            val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            return "ORD$now-${(1000..9999).random()}"
        }
    }
}