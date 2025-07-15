package com.paybook.application.dto.response;

import com.paybook.domain.entity.Category;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class TransactionsResponse {

    public static class Transactions {
        public UUID id;
        public String description;
        public BigDecimal amount;
        public Category category;
        public LocalDateTime createdAt;
    }
}
