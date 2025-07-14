package com.paybook.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class AuthResponse {
    private AuthData data;
    private String message;
    private String timestamp;
    private Integer status;



    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthData {
        private String accessToken;
        private String refreshToken;
    }
}
