package com.paybook.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paybook.application.dto.response.AuthResponse;
import com.paybook.application.usecase.AuthUseCase;
import com.paybook.domain.entity.User;
import com.paybook.domain.exception.InvalidCodeException;
import com.paybook.domain.exception.MissingFieldException;
import com.paybook.infrastructure.dao.UserDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthService implements AuthUseCase {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final RedisTemplate<String, String>  redisTemplate;
    private final UserDao userDao;
    private final ObjectMapper objectMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.refresh-token.expiration}")
    private Long jwtRefreshTokenExpiration;

    @Value("${jwt.access-token.expiration}")
    private Long jwtAccessTokenExpiration;


    public AuthService(RedisTemplate<String, String> redisTemplate, UserDao userDao,
                       BCryptPasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.userDao = userDao;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
    }


    private String generateJwtToken(User user, Long expirationInSeconds) {
        long expirationMillis = expirationInSeconds * 1000; // Convert to milliseconds
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(SignatureAlgorithm.HS256, jwtSecret).compact();
    }

    @Override
    public AuthResponse authenticate(String code) {
        String json = redisTemplate.opsForValue().get(code);
        if (json == null) {
            throw new InvalidCodeException("Kod noto'g'ri kiritilgan yoki muddati o'tgan!");
        }

        try {
            Map<String, String> data = objectMapper.readValue(json, HashMap.class);


            if (!data.containsKey("telegram_id") || data.get("telegram_id") == null) {
                throw new MissingFieldException("telegram_id");
            }
            if (!data.containsKey("phone_number") || data.get("phone_number") == null) {
                throw new MissingFieldException("phone_number");
            }
            if (!data.containsKey("name") || data.get("name") == null) {
                throw new MissingFieldException("name");
            }


            String telegramId = String.valueOf(data.get("telegram_id"));
            String phoneNumber = String.valueOf(data.get("phone_number"));
            String name = String.valueOf(data.get("name"));


            User user = userDao.findByTelegramId(telegramId)
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setTelegramId(telegramId);
                        newUser.setName(name);
                        newUser.setPhoneNumber(phoneNumber);
                        return userDao.save(newUser);
                    });

            String token = generateJwtToken(user, jwtRefreshTokenExpiration);

            AuthResponse.AuthData authData = AuthResponse.AuthData.builder()
                    .refreshToken(token)
                    .build();

            AuthResponse authResponse = new AuthResponse();
            authResponse.setData(authData);
            authResponse.setMessage("Muaffaqqiyatli kirildi!");
            authResponse.setTimestamp(LocalDateTime.now().toString());
            authResponse.setStatus(200);

            return authResponse;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse authentication data", e);
        }
    }

    @Override
    public AuthResponse getAccessToken(String refreshToken) {
        try {
            // Parse refresh token
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(refreshToken);

            log.info(String.valueOf(claimsJws.getBody().getExpiration()));

            // Get user id from JWT
            UUID userId = UUID.fromString(claimsJws.getBody().getSubject());

            // Get user from database
            Optional<User> user = Optional.ofNullable(userDao.findById(userId));

            if (user.isEmpty()) {
                throw new RuntimeException("Bunday user topilmadi!");
            }

            // Generate new access token
            String accessToken = generateJwtToken(user.orElse(null), jwtAccessTokenExpiration);

            // Build response
            AuthResponse.AuthData authData = AuthResponse.AuthData.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            AuthResponse authResponse = new AuthResponse();
            authResponse.setData(authData);
            authResponse.setMessage("Access token muvaffaqiyatli yaratildi!");
            authResponse.setTimestamp(LocalDateTime.now().toString());
            authResponse.setStatus(200);

            return authResponse;

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new InvalidCodeException("Refresh token muddati o'tgan!");
        } catch (io.jsonwebtoken.JwtException e) {
            throw new InvalidCodeException("Noto'g'ri refresh token!");
        }
    }
}
