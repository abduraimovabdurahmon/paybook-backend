package com.paybook.domain.entity;

import com.paybook.application.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Category entity - represents transaction categories in the system
 * (e.g., Income, Expense, Debt categories)
 */
@Data
@Entity
@Table(name = "category")
public class Category {

    /**
     * Unique identifier for the category
     * Stored as UUID in database
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Unique keyword identifier for the category
     * Used for internal reference in the application
     * Example: "food", "salary", "transport"
     */
    @Column(name = "keyword", unique = true, nullable = false)
    private String keyword;

    /**
     * Display name of the category
     * Shown to users in the interface
     * Example: "Food & Dining", "Monthly Salary"
     */
    @Column(name = "title")
    private String title;

    /**
     * Transaction type of the category
     * Uses TransactionType enum (INCOME, EXPENSE, DEBT)
     * Stored as string in database (e.g. "INCOME")
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    /**
     * Icon identifier for the category
     * Used for displaying in UI
     * Example: "food-icon", "transport-icon"
     */
    @Column(name = "icon", nullable = false)
    private String icon;

    /**
     * Background color for the category in UI
     * Stored as hex color code (e.g. "#FF5733")
     * or color name (e.g. "red")
     */
    @Column(name = "bg_color", nullable = false)
    private String bgColor;

    /**
     * Creation timestamp of the category
     * Automatically set to current time when created
     * Uses database server's timezone (configure Tashkent time at DB level)
     */
    @Column(name = "created_at", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}