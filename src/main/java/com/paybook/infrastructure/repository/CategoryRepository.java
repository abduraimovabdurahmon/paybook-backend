package com.paybook.infrastructure.repository;

import com.paybook.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
