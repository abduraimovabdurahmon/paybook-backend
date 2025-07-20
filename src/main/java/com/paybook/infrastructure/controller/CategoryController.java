package com.paybook.infrastructure.controller;

import com.paybook.application.dto.response.CategoryResponse;
import com.paybook.application.usecase.CategoryUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryUseCase categoryUseCase;

    @GetMapping("list")
    public List<CategoryResponse> listCategories() {
        return categoryUseCase.listCategories();
    }

}
