package com.paybook.infrastructure.controller;

import com.paybook.application.dto.response.BalanceResponse;
import com.paybook.application.dto.response.DebtBalanceResponce;
import com.paybook.application.dto.response.MonthsResponse;
import com.paybook.application.dto.response.TransactionsResponse;
import com.paybook.application.usecase.TransactionUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {


    private final TransactionUseCase transactionUseCase;

    @Autowired
    public TransactionController(@Qualifier("transactionService") TransactionUseCase transactionUseCase) {
        this.transactionUseCase = transactionUseCase;
    }

    // Helper method to extract common logic
    private record DateRange(LocalDate startDate, LocalDate endDate) {}

    private DateRange getUserIdAndDateRange() {
        String monthParam = Objects.requireNonNull((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getParameter("month");

        if (monthParam == null || monthParam.isEmpty()) {
            throw new IllegalArgumentException("month parametri majburiy!");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate startDate = LocalDate.parse(monthParam, formatter);
        LocalDate endDate = startDate.plusMonths(1);

        return new DateRange(startDate, endDate);
    }

    // Oylar ro'yxatini yuboradi
    @GetMapping("months")
    private List<MonthsResponse> months() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return transactionUseCase.getMonths(userId);
    }

    // Umumiy balansni yuboradi
    @GetMapping("balance")
    private BalanceResponse getFullBalance() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        DateRange dateRange = getUserIdAndDateRange();
        return transactionUseCase.getFullBalance(userId, dateRange.startDate(), dateRange.endDate());
    }

    // Income transactions
    @GetMapping("income/balance")
    private BalanceResponse getIncomeBalance() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        DateRange dateRange = getUserIdAndDateRange();
        return transactionUseCase.getIncomeBalance(userId, dateRange.startDate(), dateRange.endDate());
    }

    // Expense transactions
    @GetMapping("expense/balance")
    private BalanceResponse getExpenseBalance() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        DateRange dateRange = getUserIdAndDateRange();
        return transactionUseCase.getExpenseBalance(userId, dateRange.startDate(), dateRange.endDate());
    }


    // Debt transactions
    @GetMapping("debt/balance")
    protected DebtBalanceResponce getDebtBalance() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        DateRange dateRange = getUserIdAndDateRange();
        return transactionUseCase.getDebtBalance(userId, dateRange.startDate(), dateRange.endDate());
    }


}