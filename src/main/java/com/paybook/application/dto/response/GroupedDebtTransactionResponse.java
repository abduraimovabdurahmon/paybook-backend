package com.paybook.application.dto.response;


import com.paybook.application.dto.transaction.DebtTransactions;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupedDebtTransactionResponse {
    private String dateKey;
    List<DebtTransactions> transactions;
}
