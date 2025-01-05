package com.javaacademy.cryptowallet.storage;

import com.javaacademy.cryptowallet.entity.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Data
public class UserStorage {
    private final Map<String, User> data = new HashMap<>();
}
