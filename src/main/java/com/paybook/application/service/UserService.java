package com.paybook.application.service;

import com.paybook.application.dto.request.UserDetailsUpdateRequest;
import com.paybook.application.dto.response.ErrorResponse;
import com.paybook.application.dto.response.UserDetailsResponse;
import com.paybook.application.usecase.UserUseCase;
import com.paybook.domain.entity.User;
import com.paybook.domain.exception.UserNotFoundException;
import com.paybook.domain.exception.UsernameValidationException;
import com.paybook.infrastructure.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService implements UserUseCase {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserDao userDao;

    // Regex patterns for username validation
    private static final Pattern STARTS_WITH_NUMBER = Pattern.compile("^\\d.*");
    private static final Pattern VALID_CHARACTERS = Pattern.compile("^[a-zA-Z_]+$");
    private static final int MAX_USERNAME_LENGTH = 20;

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
            log.error("Bunday id li user topilmadi: {}", userId);
            throw new UserNotFoundException(
                    new ErrorResponse(
                            "User not found",
                            404,
                            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    )
            );
        }

        log.info("User ma'lumotlari yangilanmoqda: {}", userId);

        // Validate username if provided
        if (userDetails.getUsername() != null) {
            String username = userDetails.getUsername();

            // Check if username starts with a number
            if (STARTS_WITH_NUMBER.matcher(username).matches()) {
                log.error("Username validation failed for userId: {}. Username starts with a number: {}", userId, username);
                throw new UsernameValidationException(
                        new ErrorResponse(
                                "Username raqam bilan boshlanmasligi kerak!",
                                400,
                                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        )
                );
            }

            // Check if username contains only Latin letters and underscores
            if (!VALID_CHARACTERS.matcher(username).matches()) {
                log.error("Username validation failed for userId: {}. Username contains invalid characters: {}", userId, username);
                throw new UsernameValidationException(
                        new ErrorResponse(
                                "Usernameda faqat lotin harflari ishlatilsin!",
                                400,
                                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        )
                );
            }

            // Check username length
            if (username.length() > MAX_USERNAME_LENGTH) {
                log.error("Username validation failed for userId: {}. Username exceeds 20 characters: {}", userId, username);
                throw new UsernameValidationException(
                        new ErrorResponse(
                                "Username 20 ta belgidan oshmasligi kerak!",
                                400,
                                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        )
                );
            }

            log.info("Updating username to: {}", username);
            user.setUsername(username);
            userDao.updateUsername(user.getId(), username);
        }

        // Update name if provided
        if (userDetails.getName() != null) {
            log.info("Updating name to: {}", userDetails.getName());
            userDao.updateUserName(user.getId(), userDetails.getName());
        }

        log.info("Updated user details for userId: {}", userId);
        return getUserDetails(userId);
    }


}