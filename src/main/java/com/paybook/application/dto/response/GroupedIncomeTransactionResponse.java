package com.paybook.application.dto.response;

import com.paybook.application.dto.transaction.IncomeTransaction;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GroupedIncomeTransactionResponse {
    private String dateKey;
    private List<IncomeTransaction> transactions;

}

