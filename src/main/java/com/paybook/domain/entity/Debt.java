package com.paybook.domain.entity;

import com.paybook.application.enums.CurrencyType;
import com.paybook.application.enums.DebtType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "debt")
public class Debt {

    /**
     * Unique identifier for the debt
     * Stored as UUID in database
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The user who owns this income record
     * Many income records can belong to one user (Many-to-One relationship)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // type
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DebtType type;

    // Description - UI da ko'rinadigan description
    @Column(name = "description", nullable = false)
    private String description;

    // amount
    @Column(name = "amount", nullable = false, precision = 20, scale = 2)
    private BigDecimal amount;


    // currency
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private CurrencyType currency;


    // is paid
    @Column(name = "is_paid", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isPaid = false;


    // to'lanishi kerak bo'lgan sana
    @Column(name = "due_date")
    private LocalDateTime dueDate;


    /**
     * Creation timestamp of the income
     * Automatically set to current time when created
     * Uses database server's timezone (configure Tashkent time at DB level)
     */
    @Column(name = "created_at", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;



}
