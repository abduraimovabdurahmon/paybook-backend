package com.paybook.application.usecase;

import com.paybook.application.dto.response.CategoryResponse;
import org.springframework.context.annotation.Bean;

import java.util.List;


public interface CategoryUseCase {

    List<CategoryResponse> listCategories();
}
