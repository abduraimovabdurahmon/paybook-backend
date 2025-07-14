package com.paybook.application.service;

import com.paybook.application.dto.response.BalanceResponse;
import com.paybook.application.dto.response.MonthsResponse;
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
            response.setTitle(formatter.formatMonthTitle(monthDate));
            response.setValue(monthDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));

            monthsList.add(response);
        }

        return monthsList;
    }


    // Umumiy balansni olib keladi
    @Override
    public BalanceResponse getFullBalance(String userId) {
        // RequestParam orqali kelgan "month"ni olish uchun
        String monthParam = ServletRequestAttributes.class.cast(RequestContextHolder.getRequestAttributes())
                .getRequest().getParameter("month");

        if (monthParam == null || monthParam.isEmpty()) {
            throw new IllegalArgumentException("Month parametri majburiy!");
        }

        // 'yyyy.MM.dd' formatidan LocalDate ga parse qilamiz
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate startDate = LocalDate.parse(monthParam, formatter);
        LocalDate endDate = startDate.plusMonths(1); // 1 oy qo‘shamiz

        // SQL queryni DAO orqali chaqiramiz
        BigDecimal totalBalance = transactionDao.getTotalBalance(userId, startDate, endDate);

        BalanceResponse response = new BalanceResponse();
        response.setBalance(totalBalance);
        return response;
    }


}
