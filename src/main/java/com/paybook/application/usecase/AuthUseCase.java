package com.paybook.application.usecase;

import com.paybook.application.dto.response.AuthResponse;

public interface AuthUseCase {
    AuthResponse authenticate(String code);

    AuthResponse getAccessToken(String refreshToken);
}
