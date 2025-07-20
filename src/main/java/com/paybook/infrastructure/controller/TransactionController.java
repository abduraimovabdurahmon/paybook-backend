package com.paybook.infrastructure.controller;

import com.paybook.application.dto.response.*;
import com.paybook.application.usecase.TransactionUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
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

    // Income transactions balance
    @GetMapping("income/balance")
    private BalanceResponse getIncomeBalance() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        DateRange dateRange = getUserIdAndDateRange();
        return transactionUseCase.getIncomeBalance(userId, dateRange.startDate(), dateRange.endDate());
    }

    // Expense transactions balance
    @GetMapping("expense/balance")
    private BalanceResponse getExpenseBalance() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        DateRange dateRange = getUserIdAndDateRange();
        return transactionUseCase.getExpenseBalance(userId, dateRange.startDate(), dateRange.endDate());
    }


    // Debt transactions balance
    @GetMapping("debt/balance")
    protected DebtBalanceResponse getDebtBalance() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        DateRange dateRange = getUserIdAndDateRange();
        return transactionUseCase.getDebtBalance(userId, dateRange.startDate(), dateRange.endDate());
    }


    @GetMapping("/income")
    public List<GroupedIncomeTransactionResponse> getIncomeTransactions() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        DateRange dateRange = getUserIdAndDateRange();
        return transactionUseCase.getIncomeTransactions(userId, dateRange.startDate(), dateRange.endDate());
    }

    @GetMapping("/expense")
    public List<GroupedExpenseTransactionResponse> getExpenseTransactions() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        DateRange dateRange = getUserIdAndDateRange();
        List<GroupedExpenseTransactionResponse> transactions = transactionUseCase.getExpenseTransactions(userId, dateRange.startDate(), dateRange.endDate());
        return transactions != null ? transactions : Collections.emptyList();
    }

    @GetMapping("/debt")
    public List<GroupedDebtTransactionResponse> getDebtTransactions() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        DateRange dateRange = getUserIdAndDateRange();
        return transactionUseCase.getDebtTransactions(userId, dateRange.startDate(), dateRange.endDate());
    }


}