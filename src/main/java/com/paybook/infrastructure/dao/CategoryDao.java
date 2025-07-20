package com.paybook.infrastructure.dao;

import com.paybook.application.dto.response.CategoryResponse;
import com.paybook.application.enums.TransactionType;
import com.paybook.domain.entity.Category;
import com.paybook.infrastructure.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CategoryDao {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryResponse> listCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(category -> {
            CategoryResponse response = new CategoryResponse();
            response.setId(category.getId());
            response.setBgColor(category.getBgColor());
            response.setIcon(category.getIcon());
            response.setKeyword(category.getKeyword());
            response.setTitle(category.getTitle());
            response.setType(TransactionType.valueOf(String.valueOf(category.getType())));
            return response;
        }).toList();
    }
}
