package com.paybook.application.service;

import com.paybook.application.dto.request.UserDetailsUpdateRequest;
import com.paybook.application.dto.response.UserDetailsResponse;
import com.paybook.application.usecase.UserUseCase;
import com.paybook.domain.entity.User;
import com.paybook.infrastructure.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements UserUseCase {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        super();
        this.userDao = userDao;
    }

    @Override  // <- @Override annotatsiyasini qo'shing
    public UserDetailsResponse getUserDetails(String userId) {  // <- public qiling
        User user = userDao.findById(UUID.fromString(userId));
        if (user == null) {
            return null; // yoki istisno tashlang
        }
        UserDetailsResponse response = new UserDetailsResponse();
        response.setName(user.getName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setUsername(user.getUsername());
        return response;
    }

    @Override
    public UserDetailsResponse updateUserDetails(String userId, UserDetailsUpdateRequest userDetails) {
        User user = userDao.findById(UUID.fromString(userId));
        if (user == null) {
            log.error("User not found for userId: {}", userId);
            throw new RuntimeException("User not found");
        }

        log.info("Updating user details for userId: {}", userId);
        if (userDetails.getName() != null) {
            log.info("Updating name to: {}", userDetails.getName());
            userDao.updateUserName(user.getId(), userDetails.getName());
        }
        if (userDetails.getUsername() != null) {
            log.info("Updating username to: {}", userDetails.getUsername());
            user.setUsername(userDetails.getUsername()); // Fixed: Update username, not phoneNumber
            userDao.updateUsername(user.getId(), userDetails.getUsername());
        }

        log.info("Updated user details for userId: {}", userId);
        return getUserDetails(userId);
    }


}