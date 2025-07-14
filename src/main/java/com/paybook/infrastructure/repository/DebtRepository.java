package com.paybook.infrastructure.repository;

import com.paybook.domain.entity.Debt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DebtRepository extends JpaRepository<Debt, Long> {
}
