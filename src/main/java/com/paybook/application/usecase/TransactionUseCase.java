package com.paybook.application.usecase;

import com.paybook.application.dto.response.*;

import java.time.LocalDate;
import java.util.List;

public interface TransactionUseCase {

    List<MonthsResponse> getMonths(String userId);

    BalanceResponse getFullBalance(String userId, LocalDate startDate, LocalDate endDate);

    BalanceResponse getIncomeBalance(String userId, LocalDate startDate, LocalDate endDate);

    BalanceResponse getExpenseBalance(String userId, LocalDate startDate, LocalDate endDate);

    DebtBalanceResponse getDebtBalance(String userId, LocalDate startDate, LocalDate endDate);

    List<IncomeTransaction> getIncomeTransactions(String userId, LocalDate startDate, LocalDate endDate);
}
