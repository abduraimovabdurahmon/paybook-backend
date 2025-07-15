package com.paybook.application.service;

import com.paybook.application.dto.response.BalanceResponse;
import com.paybook.application.dto.response.DebtBalanceResponce;
import com.paybook.application.dto.response.MonthsResponse;
import com.paybook.application.dto.response.TransactionsResponse;
import com.paybook.application.usecase.TransactionUseCase;
import com.paybook.infrastructure.dao.TransactionDao;
import com.paybook.infrastructure.utils.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
    public BalanceResponse getIncomeBalance(String userId, LocalDate StartDate, LocalDate EndDate) {
        BigDecimal totalBalance = transactionDao.getTotalIncomeBalance(userId, StartDate, EndDate);
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
    public DebtBalanceResponce getDebtBalance(String userId, LocalDate localDate, LocalDate localDate1) {
        return transactionDao.getTotalDebtBalance(userId, localDate, localDate1);
    }


}
