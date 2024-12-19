package com.javaacademy.cryptowallet.storage;

import com.javaacademy.cryptowallet.entity.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Data
@Slf4j
public class UserStorage {
    private final Map<String, User> storage = new HashMap<>();

    public Optional<User> getUser(String login) {
        return Optional.ofNullable(storage.get(login));
    }

    public void saveUser(User user) {
        if (storage.containsKey(user.getLogin())) {
            throw new RuntimeException("Пользователь с таким логином уже есть");
        }
        storage.put(user.getLogin(), user);
        log.info("Пользователь: {} успешно добавлен", user.getLogin());
    }
}
