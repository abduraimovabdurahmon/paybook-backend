package com.paybook.application.dto.transaction;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ExpenseTransaction {
    private UUID id;
    private String icon;
    private String bgColor;
    private String title;
    private String description;
    private BigDecimal amount;
    private String createdAt;
    private String time;
}
