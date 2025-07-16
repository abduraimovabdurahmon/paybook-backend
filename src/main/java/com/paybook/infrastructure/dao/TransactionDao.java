package com.paybook.infrastructure.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paybook.application.dto.response.DebtBalanceResponse;
import com.paybook.application.dto.response.GroupedIncomeTransactionResponse;
import com.paybook.application.dto.response.IncomeTransaction;
import com.paybook.infrastructure.repository.DebtRepository;
import com.paybook.infrastructure.repository.ExpenseRepository;
import com.paybook.infrastructure.repository.IncomeRepository;
import com.paybook.infrastructure.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class TransactionDao {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private DebtRepository debtRepository;


    @PersistenceContext
    private EntityManager entityManager;

    private final ObjectMapper objectMapper;

    public TransactionDao(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public BigDecimal getTotalBalance(String userId, LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT (
                    (SELECT COALESCE(SUM(i.amount), 0) FROM income i 
                     WHERE i.user_id = :userId AND i.created_at BETWEEN :start AND :end)
                    -
                    (SELECT COALESCE(SUM(e.amount), 0) FROM expense e 
                     WHERE e.user_id = :userId AND e.created_at BETWEEN :start AND :end)
                    +
                    (SELECT COALESCE(SUM(d.amount), 0) FROM debt d 
                     WHERE d.user_id = :userId AND d.created_at BETWEEN :start AND :end AND d.type = 'LEND')
                    -
                    (SELECT COALESCE(SUM(d.amount), 0) FROM debt d 
                     WHERE d.user_id = :userId AND d.created_at BETWEEN :start AND :end AND d.type = 'BORROW')
                ) AS total_balance
                """;

        Query query = entityManager.createNativeQuery(sql);


        Map<String, Object> params = Map.of(
                "userId", UUID.fromString(userId),
                "start", startDate,
                "end", endDate
        );

        // Har bir parametrni queryga qo‘shamiz
        params.forEach(query::setParameter);

        Object result = query.getSingleResult();
        return (result != null) ? new BigDecimal(result.toString()) : BigDecimal.ZERO;
    }


    public BigDecimal getTotalIncomeBalance(String userId, LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT COALESCE(SUM(i.amount), 0) AS total_income
                FROM income i 
                WHERE i.user_id = :userId AND i.created_at BETWEEN :start AND :end
                """;

        Query query = entityManager.createNativeQuery(sql);

        Map<String, Object> params = Map.of(
                "userId", UUID.fromString(userId),
                "start", startDate,
                "end", endDate
        );

        params.forEach(query::setParameter);

        Object result = query.getSingleResult();
        return (result != null) ? new BigDecimal(result.toString()) : BigDecimal.ZERO;
    }

    public BigDecimal getTotalExpenseBalance(String userId, LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT COALESCE(SUM(i.amount), 0) AS total_income
                FROM expense i 
                WHERE i.user_id = :userId AND i.created_at BETWEEN :start AND :end
                """;

        Query query = entityManager.createNativeQuery(sql);

        Map<String, Object> params = Map.of(
                "userId", UUID.fromString(userId),
                "start", startDate,
                "end", endDate
        );

        params.forEach(query::setParameter);

        Object result = query.getSingleResult();
        return (result != null) ? new BigDecimal(result.toString()) : BigDecimal.ZERO;
    }

    public DebtBalanceResponse getTotalDebtBalance(String userId, LocalDate startDate, LocalDate endDate) {
        // lend and borrow
        String sql = """
                SELECT 
                    COALESCE(SUM(CASE WHEN d.type = 'LEND' THEN d.amount ELSE 0 END), 0) AS total_lend,
                    COALESCE(SUM(CASE WHEN d.type = 'BORROW' THEN d.amount ELSE 0 END), 0) AS total_borrow
                FROM debt d 
                WHERE d.user_id = :userId AND d.created_at BETWEEN :start AND :end
                """;
        Query query = entityManager.createNativeQuery(sql);
        Map<String, Object> params = Map.of(
                "userId", UUID.fromString(userId),
                "start", Timestamp.valueOf(startDate.atStartOfDay()),
                "end", Timestamp.valueOf(endDate.atStartOfDay())
        );

        params.forEach(query::setParameter);
        Object[] result = (Object[]) query.getSingleResult();
        BigDecimal totalLend = (result[0] != null) ? new BigDecimal(result[0].toString()) : BigDecimal.ZERO;
        BigDecimal totalBorrow = (result[1] != null) ? new BigDecimal(result[1].toString()) : BigDecimal.ZERO;
        DebtBalanceResponse response = new DebtBalanceResponse();
        response.setTotalLend(totalLend);
        response.setTotalBorrow(totalBorrow);
        return response;
    }

    public List<GroupedIncomeTransactionResponse> getIncomeTransactions(String userId, LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT
                    CONCAT(
                        TO_CHAR(i.created_at, 'DD'),
                        ' - ',
                        CASE EXTRACT(MONTH FROM i.created_at)
                            WHEN 1 THEN 'Yanvar'
                            WHEN 2 THEN 'Fevral'
                            WHEN 3 THEN 'Mart'
                            WHEN 4 THEN 'Aprel'
                            WHEN 5 THEN 'May'
                            WHEN 6 THEN 'Iyun'
                            WHEN 7 THEN 'Iyul'
                            WHEN 8 THEN 'Avgust'
                            WHEN 9 THEN 'Sentabr'
                            WHEN 10 THEN 'Oktabr'
                            WHEN 11 THEN 'Noyabr'
                            WHEN 12 THEN 'Dekabr'
                        END
                    ) AS date_key,
                    JSON_AGG(
                        JSON_BUILD_OBJECT(
                            'id', i.id,
                            'icon', c.icon,
                            'bgColor', c.bg_color,
                            'title', c.title,
                            'description', i.description,
                            'amount', i.amount,
                            'createdAt', TO_CHAR(i.created_at, 'YYYY-MM-DD HH24:MI'),
                            'time', TO_CHAR(i.created_at, 'HH24:MI')
                        )
                        ORDER BY i.created_at DESC
                    ) AS transactions
                FROM income i
                LEFT JOIN category c ON i.category_id = c.id
                WHERE i.user_id = :userId
                  AND i.created_at BETWEEN :startDate AND :endDate
                GROUP BY 
                    CONCAT(
                        TO_CHAR(i.created_at, 'DD'),
                        ' - ',
                        CASE EXTRACT(MONTH FROM i.created_at)
                            WHEN 1 THEN 'Yanvar'
                            WHEN 2 THEN 'Fevral'
                            WHEN 3 THEN 'Mart'
                            WHEN 4 THEN 'Aprel'
                            WHEN 5 THEN 'May'
                            WHEN 6 THEN 'Iyun'
                            WHEN 7 THEN 'Iyul'
                            WHEN 8 THEN 'Avgust'
                            WHEN 9 THEN 'Sentabr'
                            WHEN 10 THEN 'Oktabr'
                            WHEN 11 THEN 'Noyabr'
                            WHEN 12 THEN 'Dekabr'
                        END
                    )
                ORDER BY MAX(i.created_at) DESC;
                """;

        Query query = entityManager.createNativeQuery(sql);

        Map<String, Object> params = Map.of(
                "userId", UUID.fromString(userId),
                "startDate", Timestamp.valueOf(startDate.atStartOfDay()),
                "endDate", Timestamp.valueOf(endDate.atTime(23, 59, 59))
        );

        params.forEach(query::setParameter);

        List<Object> result = query.getResultList();
        if (result == null || result.isEmpty()) {
            return List.of();
        }

        List<GroupedIncomeTransactionResponse> responses = new ArrayList<>();
        for (Object obj : result) {
            Object[] row = (Object[]) obj;
            GroupedIncomeTransactionResponse response = new GroupedIncomeTransactionResponse();
            response.setDateKey((String) row[0]);

            try {
                List<IncomeTransaction> transactions = objectMapper.readValue(
                        (String) row[1],
                        new TypeReference<List<IncomeTransaction>>() {}
                );
                response.setTransactions(transactions);
                responses.add(response);
            } catch (Exception e) {
                throw new RuntimeException("JSON parsing error", e);
            }
        }

        return responses;
    }
}
