package com.paybook.application.enums;

import lombok.Getter;

@Getter
public enum CurrencyType {
    UZS("UZS"),
    USD("USD"),
    GBP("GBP"),
    EUR("EUR");

    private final String displayName;

    CurrencyType(String displayName) {
        this.displayName = displayName;
    }

}
