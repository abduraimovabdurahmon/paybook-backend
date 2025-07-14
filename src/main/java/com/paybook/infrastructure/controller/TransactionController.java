package com.paybook.infrastructure.controller;

import com.paybook.application.dto.response.BalanceResponse;
import com.paybook.application.dto.response.MonthsResponse;
import com.paybook.application.usecase.TransactionUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionUseCase transactionUseCase;


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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return transactionUseCase.getFullBalance(userId);
    }

}
