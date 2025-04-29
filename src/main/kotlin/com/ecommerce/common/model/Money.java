package com.ecommerce.common.model;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.function.Function;

@Embeddable
public class Money {
    private BigDecimal amount;

    public static Money ZERO = new Money(BigDecimal.ZERO);

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public static <T> Money sum(Collection<T> bags, Function<T, Money> monetary) {
        return bags.stream()
                .map(monetary)
                .reduce(Money.ZERO, Money::plus);
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

    public Money multiply(BigDecimal number) {
        if (number.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ArithmeticException("can't add zero or negative money");
        }
        return new Money(this.amount.multiply(number));
    }

    public boolean isLessThan(Money money) {
        return this.amount.compareTo(money.amount) < 0;
    }

    public boolean isGreaterThanOrEqual(Money money) {
        return this.amount.compareTo(money.amount) >= 0;
    }
}
