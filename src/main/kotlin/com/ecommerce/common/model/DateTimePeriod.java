package com.ecommerce.common.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Embeddable
@AllArgsConstructor
public class DateTimePeriod {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    protected DateTimePeriod() {
    }

    public boolean isWithin(LocalDateTime dateTime) {
        return startDateTime.isEqual(dateTime) || endDateTime.isEqual(dateTime) ||
                (startDateTime.isBefore(dateTime) && endDateTime.isAfter(dateTime));
    }
}
