package com.paybook.application.dto.response;


import com.paybook.application.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryResponse {
    private UUID id;
    private String bgColor;
    private String icon;
    private String keyword;
    private String title;
    private TransactionType type;
}
