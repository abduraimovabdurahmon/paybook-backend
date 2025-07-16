package com.paybook.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class GroupedIncomeTransactionResponse {
    private String dateKey;
    private List<IncomeTransaction> transactions;

}

