package com.ecommerce.common.model;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Money {
    private BigDecimal amount;

    public static Money ZERO = new Money(BigDecimal.ZERO);

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    protected Money() {
    }

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Money plus(Money money) {
        if (money.amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ArithmeticException("can't add zero or negative money");
        }
        return new Money(this.amount.add(money.amount));
    }

    public Money minus(Money money) {
        if (money.amount.compareTo(this.amount) > 0) {
            throw new ArithmeticException("can't subtract more than available amount");
        }
        return new Money(this.amount.subtract(money.amount));
    }
}
