package com.paybook.application.usecase;

import com.paybook.application.dto.request.UserDetailsUpdateRequest;
import com.paybook.application.dto.response.UserDetailsResponse;

public interface UserUseCase {
    UserDetailsResponse getUserDetails(String userId);

    UserDetailsResponse updateUserDetails(String userId, UserDetailsUpdateRequest userDetails);
}
