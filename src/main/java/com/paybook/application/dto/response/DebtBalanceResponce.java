package com.paybook.application.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DebtBalanceResponce {
    BigDecimal totalLend;
    BigDecimal totalBorrow;
}
