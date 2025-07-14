package com.paybook.infrastructure.repository;

import com.paybook.domain.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, String> {
}
