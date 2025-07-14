package com.paybook.domain.exception;

import com.paybook.application.dto.response.ErrorResponse;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public UserNotFoundException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

}