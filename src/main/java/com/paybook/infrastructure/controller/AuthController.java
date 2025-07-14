package com.paybook.infrastructure.controller;

import com.paybook.application.dto.request.AuthRequest;
import com.paybook.application.dto.request.RefreshTokenRequest;
import com.paybook.application.dto.response.AuthResponse;
import com.paybook.application.usecase.AuthUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthUseCase authUseCase;



    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse authResponse = authUseCase.authenticate(request.getCode());
        return ResponseEntity.ok(authResponse);
    }


    @PostMapping("/refresh")
    ResponseEntity<AuthResponse> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        AuthResponse authResponse = authUseCase.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(authResponse);
    }

}
