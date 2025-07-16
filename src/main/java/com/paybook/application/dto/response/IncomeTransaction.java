package com.paybook.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
public class IncomeTransaction {
    private UUID id;
    private String icon;
    private String bgColor;
    private String title;
    private String description;
    private BigDecimal amount;
    private String createdAt;
    private String time;

}