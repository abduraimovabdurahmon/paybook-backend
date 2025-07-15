package com.paybook.application.usecase;

import com.paybook.application.dto.response.BalanceResponse;
import com.paybook.application.dto.response.DebtBalanceResponce;
import com.paybook.application.dto.response.MonthsResponse;
import com.paybook.application.dto.response.TransactionsResponse;

import java.time.LocalDate;
import java.util.List;

public interface TransactionUseCase {

    List<MonthsResponse> getMonths(String userId);

    BalanceResponse getFullBalance(String userId, LocalDate startDate, LocalDate endDate);

    BalanceResponse getIncomeBalance(String userId, LocalDate StartDate, LocalDate EndDate);

    BalanceResponse getExpenseBalance(String userId, LocalDate StartDate, LocalDate EndDate);

    DebtBalanceResponce getDebtBalance(String userId, LocalDate localDate, LocalDate localDate1);
}
