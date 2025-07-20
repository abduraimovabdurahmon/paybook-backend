package com.paybook.application.dto.response;

import com.paybook.application.dto.transaction.ExpenseTransaction;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupedExpenseTransactionResponse {
    private String dateKey;
    private List<ExpenseTransaction> transactions;

}
