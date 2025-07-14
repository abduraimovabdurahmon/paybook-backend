package com.paybook.application.usecase;

import com.paybook.application.dto.response.BalanceResponse;
import com.paybook.application.dto.response.MonthsResponse;

import java.util.List;

public interface TransactionUseCase {

    List<MonthsResponse> getMonths(String userId);

    BalanceResponse getFullBalance(String userId);
}
