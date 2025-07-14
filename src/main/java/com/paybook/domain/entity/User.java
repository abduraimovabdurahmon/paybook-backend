package com.paybook.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "users",
        indexes = {
                @Index(name = "idx_telegram_id", columnList = "telegram_id"),
                @Index(name = "idx_username", columnList = "username")
        },
        uniqueConstraints = @UniqueConstraint(columnNames = "telegram_id"))
public class User {

    // userning id'si(butun dastur uchun)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // userning ro'yxatdan o'tgan telegram id'si
    @Column(name = "telegram_id", unique = true, nullable = false)
    private String telegramId;

    // userning telegramdan olingan ismi(firstname + lastname)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    // userning username'i butun dastur uchun
    @Column(name = "username")
    private String username;

}
