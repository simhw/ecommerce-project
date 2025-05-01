package com.ecommerce.common.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Embeddable
@AllArgsConstructor
public class Address {
    private String street;
    private String city;

    protected Address() {
    }
}
