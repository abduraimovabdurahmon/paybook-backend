package com.paybook.application.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class IncomeTransactionsResponse {

    private List<IncomeTransaction> transactions;

    public IncomeTransactionsResponse() {
    }

}
