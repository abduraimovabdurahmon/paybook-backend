package com.paybook.domain.entity;

import com.paybook.application.enums.CurrencyType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "income")
public class Income {
    /**
     * Unique identifier for the income
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


    @ManyToOne()
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;



    /**
     * Creation timestamp of the income
     * Automatically set to current time when created
     * Uses database server's timezone (configure Tashkent time at DB level)
     */
    @Column(name = "created_at", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
