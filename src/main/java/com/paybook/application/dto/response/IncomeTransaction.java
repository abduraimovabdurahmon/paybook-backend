package com.paybook.application.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class IncomeTransaction {
    private UUID id;
    private String icon;
    private String bgColor;
    private String title;
    private String description;
    private BigDecimal amount;
    private String createdAt;
}
