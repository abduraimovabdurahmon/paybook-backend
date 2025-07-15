package com.paybook.infrastructure.dao;

import com.paybook.application.dto.response.DebtBalanceResponse;
import com.paybook.application.dto.response.IncomeTransaction;
import com.paybook.application.dto.response.IncomeTransactionsResponse;
import com.paybook.domain.entity.Income;
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

    public List<IncomeTransaction> getIncomeTransactions(String userId, LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT
                    i.id,
                    c.icon,
                    c.bg_color,
                    c.title,
                    i.description,
                    i.amount,
                    TO_CHAR(i.created_at, 'YYYY-MM-DD HH24:MI') AS created_at
                FROM income i
                LEFT JOIN category c ON i.category_id = c.id
                WHERE i.user_id = :userId
                  AND i.created_at BETWEEN :startDate AND :endDate
                ORDER BY i.created_at DESC;
        """;

        Query query = entityManager.createNativeQuery(sql);

        Map<String, Object> params = Map.of(
                "userId", UUID.fromString(userId),
                "startDate", Timestamp.valueOf(startDate.atStartOfDay()),
                "endDate", Timestamp.valueOf(endDate.atStartOfDay())
        );

        params.forEach(query::setParameter);

        var transactions = query.getResultList();
        if (transactions == null || transactions.isEmpty()) {
            return List.of();
        }

        List<IncomeTransaction> transactionsResponses = new ArrayList<>();
        for (Object obj : transactions) {
            Object[] row = (Object[]) obj;
            IncomeTransaction response = new IncomeTransaction();
            response.setId(UUID.fromString(row[0].toString()));
            response.setIcon(row[1].toString());
            response.setBgColor(row[2].toString());
            response.setTitle(row[3].toString());
            response.setDescription(row[4].toString());
            response.setAmount(new BigDecimal(row[5].toString()));
            response.setCreatedAt(row[6].toString());
            transactionsResponses.add(response);
        }

        return transactionsResponses;
    }
}
