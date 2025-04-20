package com.ecommerce.common.model

import java.math.BigDecimal

data class Money(
    private val value: BigDecimal
) {
    fun plus(other: Money): Money {
        if (other.value <= BigDecimal.ZERO) {
            throw ArithmeticException()
        }
        return Money(value + other.value)
    }

    fun plus(other: BigDecimal): Money {
        if (other <= BigDecimal.ZERO) {
            throw ArithmeticException()
        }
        return Money(value + other)
    }

    fun plus(other: Long): Money {
        if (BigDecimal.valueOf(other) <= BigDecimal.ZERO) {
            throw ArithmeticException()
        }
        return Money(value + BigDecimal.valueOf(other))
    }

    fun minus(other: Money): Money {
        if (other.value < other.value) {
            throw ArithmeticException()
        }
        return Money(value - other.value)
    }

    fun minus(other: Long): Money {
        return Money(value - BigDecimal.valueOf(other.toDouble()))
    }

    fun times(other: Long): Money {
        return Money(value * BigDecimal.valueOf(other.toDouble()))
    }

}