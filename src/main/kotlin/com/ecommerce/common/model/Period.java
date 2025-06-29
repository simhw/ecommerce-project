package com.ecommerce.common.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Embeddable
@AllArgsConstructor
@Getter
public class Period {
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    protected Period() {
    }

    public boolean isWithin(LocalDateTime time) {
        // startAt <= time <= end
        return startAt.isBefore(time) && endAt.isAfter(time);
    }
}
