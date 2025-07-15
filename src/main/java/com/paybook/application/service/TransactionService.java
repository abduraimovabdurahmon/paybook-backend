package com.paybook.application.service;

import com.paybook.application.dto.response.*;
import com.paybook.application.usecase.TransactionUseCase;
import com.paybook.infrastructure.dao.TransactionDao;
import com.paybook.infrastructure.utils.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TransactionService implements TransactionUseCase {

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private Formatter formatter;

    @Override
    public List<MonthsResponse> getMonths(String userId) {
        List<MonthsResponse> monthsList = new ArrayList<>();

        // Boshlang'ich sana — hozirgi sana
        LocalDate currentDate = LocalDate.now();

        // 12 oy orqaga qarab sanaymiz
        for (int i = 11; i >= 0; i--) {
            LocalDate monthDate = currentDate.minusMonths(i).withDayOfMonth(1);

            MonthsResponse response = new MonthsResponse();
            response.setLabel(formatter.formatMonthTitle(monthDate));
            response.setValue(monthDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));

            monthsList.add(response);
        }

        return monthsList;
    }


    // Umumiy balansni olib keladi
    @Override
    public BalanceResponse getFullBalance(String userId, LocalDate startDate, LocalDate endDate) {
        BigDecimal totalBalance = transactionDao.getTotalBalance(userId, startDate, endDate);
        BalanceResponse response = new BalanceResponse();
        response.setBalance(totalBalance);
        return response;
    }

    // Foydalanuvchi uchun daromad balansini olib keladi
    @Override
    public BalanceResponse getIncomeBalance(String userId, LocalDate startDate, LocalDate endDate) {
        BigDecimal totalBalance = transactionDao.getTotalIncomeBalance(userId, startDate, endDate);
        BalanceResponse response = new BalanceResponse();
        response.setBalance(totalBalance);
        return response;
    }

    // Foydalanuvchi uchun xarajat balansini olib keladi
    @Override
    public BalanceResponse getExpenseBalance(String userId, LocalDate startDate, LocalDate endDate) {
        BigDecimal totalBalance = transactionDao.getTotalExpenseBalance(userId, startDate, endDate);
        BalanceResponse response = new BalanceResponse();
        response.setBalance(totalBalance);
        return response;
    }

    @Override
    public DebtBalanceResponse getDebtBalance(String userId, LocalDate startDate, LocalDate endDate) {
        return transactionDao.getTotalDebtBalance(userId, startDate, endDate);
    }

    @Override
    public List<IncomeTransaction> getIncomeTransactions(String userId, LocalDate startDate, LocalDate endDate) {
        List<IncomeTransaction> transactions = transactionDao.getIncomeTransactions(userId, startDate, endDate);
        if (transactions == null || transactions.isEmpty()) {
            return Collections.emptyList();
        }
        return transactions;
    }


}
