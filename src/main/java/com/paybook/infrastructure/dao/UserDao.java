package com.paybook.infrastructure.dao;

import com.paybook.domain.entity.User;
import com.paybook.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserDao {

    private final UserRepository userRepository;

    public UserDao(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByTelegramId(String telegramId) {
        return userRepository.findByTelegramId(telegramId);
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public void updateUserName(UUID id, String name) {
        User user = findById(id);
        if (user != null) {
            user.setName(name);
            userRepository.save(user);
            return;
        }
        throw new RuntimeException("User not found");
    }

    @Transactional
    public void updateUsername(UUID id, String username) {
        User user = findById(id);
        if (user != null) {
            user.setUsername(username);
            userRepository.save(user);
            return;
        }
        throw new RuntimeException("User not found");
    }
}