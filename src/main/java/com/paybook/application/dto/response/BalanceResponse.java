package com.paybook.application.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class BalanceResponse {
    BigDecimal balance;
}
