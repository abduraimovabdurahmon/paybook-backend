package com.paybook.infrastructure.controller;

import com.paybook.application.dto.request.UserDetailsUpdateRequest;
import com.paybook.application.dto.response.UserDetailsResponse;
import com.paybook.application.usecase.UserUseCase;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @GetMapping("/me")
    public UserDetailsResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        UserDetailsResponse userDetails = userUseCase.getUserDetails(userId);
        if (userDetails == null) {
            throw new RuntimeException("User not found");
        }
        return userDetails;
    }


    @PutMapping("/me")
    public UserDetailsResponse updateCurrentUser(@RequestBody UserDetailsUpdateRequest userDetailsUpdateRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        UserDetailsResponse updatedUserDetails = userUseCase.updateUserDetails(userId, userDetailsUpdateRequest);
        if (updatedUserDetails == null) {
            throw new RuntimeException("Failed to update user details");
        }
        return updatedUserDetails;
    }

}
