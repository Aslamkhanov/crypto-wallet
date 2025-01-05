package com.javaacademy.cryptowallet.service;

import com.javaacademy.cryptowallet.entity.User;
import com.javaacademy.cryptowallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public void saveNewUser(User user) {
        userRepository.saveUser(user);
    }

    public User getUserByLogin(String login) {
        return userRepository.getUser(login)
                .orElseThrow(() -> new RuntimeException("Пользователь с логином "
                        + login + " не найден"));
    }

    public void resetUsersPassword(String login, String oldPassword, String newPassword) {
        User user = getUserByLogin(login);
        if (!user.getPassword().equals(oldPassword)) {
            throw new RuntimeException("Неправильный пароль");
        }
        user.setPassword(newPassword);
        log.info("Пароль изменен!");
    }
}
