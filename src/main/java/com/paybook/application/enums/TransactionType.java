package com.paybook.application.enums;

import lombok.Getter;

@Getter
public enum TransactionType {
    INCOME("income"),     // Kirim tranzaksiyasi
    EXPENSE("expense"),    // Chiqim tranzaksiyasi
    DEBT("debt"),          // Qarz operatsiyasi
    LOAN("loan");   // Qarz olish/berish (agar kerak bo'lsa)

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

}
