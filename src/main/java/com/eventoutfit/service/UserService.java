package com.eventoutfit.service;

import com.eventoutfit.model.User;
import com.eventoutfit.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean registerUser(String username, String email, String password) {
        logger.info("Попытка регистрации: username={}, email={}", username, email);

        if (userExists(username)) {
            logger.warn("Регистрация отклонена: имя пользователя {} уже занято", username);
            return false;
        }

        if (userRepository.existsByEmail(email)) {
            logger.warn("Регистрация отклонена: email {} уже используется", email);
            return false;
        }

        if (password == null || password.isBlank() || password.length() < 8) {
            logger.warn("Регистрация отклонена: пароль меньше 8 символов");
            return false;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));

        userRepository.save(user);
        logger.info("Регистрация пользователя {} прошла успешно", username);

        return true;
    }

    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public Optional<User> findById(Long id) {
        logger.debug("Поиск пользователя по ID: {}", id);
        return userRepository.findById(id);
    }

    public User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            logger.debug("Пользователь не аутентифицирован");
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }

        return null;
    }
}