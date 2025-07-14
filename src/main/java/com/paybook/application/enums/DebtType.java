package com.paybook.application.enums;

import lombok.Getter;

@Getter
public enum DebtType {
    BORROW("BORROW"),
    LEND("LEND"),
    TEST("TEST");

    private final String displayName;

    DebtType(String displayName) {
        this.displayName = displayName;
    }

}
