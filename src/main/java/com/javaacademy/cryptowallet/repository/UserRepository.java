package com.javaacademy.cryptowallet.repository;

import com.javaacademy.cryptowallet.entity.User;
import com.javaacademy.cryptowallet.storage.UserStorage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
@RequiredArgsConstructor
@Slf4j
@Component
@Data
public class UserRepository {
    private final UserStorage userStorage;
    public Optional<User> getUser(String login) {
        return Optional.ofNullable(userStorage.getData().get(login));
    }

    public void saveUser(User user) {
        if (userStorage.getData().containsKey(user.getLogin())) {
            throw new RuntimeException("Пользователь с таким логином уже есть");
        }
        userStorage.getData().put(user.getLogin(), user);
        log.info("Пользователь: {} успешно добавлен", user.getLogin());
    }
}
