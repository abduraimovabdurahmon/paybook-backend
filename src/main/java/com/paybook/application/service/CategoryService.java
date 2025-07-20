package com.paybook.application.service;

import com.paybook.application.dto.response.CategoryResponse;
import com.paybook.application.usecase.CategoryUseCase;
import com.paybook.infrastructure.dao.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements CategoryUseCase {

    @Autowired
    private CategoryDao serviceDao;

    @Override
    public List<CategoryResponse> listCategories() {
        return serviceDao.listCategories();
    }
}
