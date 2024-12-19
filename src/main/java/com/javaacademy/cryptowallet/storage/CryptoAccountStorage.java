package com.javaacademy.cryptowallet.storage;

import com.javaacademy.cryptowallet.crypto.CryptoAccount;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Data
public class CryptoAccountStorage {
    private final Map<UUID, CryptoAccount> data = new HashMap<>();

    public void saveAccount(CryptoAccount account) {
        if (data.containsKey(account.getUniqueAccountNumber())) {
            throw new RuntimeException("Такой счет уже есть: " + account.getUniqueAccountNumber());
        }
        data.put(account.getUniqueAccountNumber(), account);
    }

    public CryptoAccount getAccount(UUID uuid) {
        return data.get(uuid);
    }

    public List<CryptoAccount> getAllAccount(String login) {
        List<CryptoAccount> accounts = new ArrayList<>();
        for (CryptoAccount account : data.values()) {
            if (account.getLogin().equals(login)) {
                accounts.add(account);
            }
        }
        return accounts;
    }
}
/**
 * Хранит: uuid -> криптосчет:
 * 3.1 Умеет сохранять счет. Если с таким uuid есть счет, то ошибка.
 * 3.2 Умеет отдавать счет по uuid.
 * 3.3. Умеет отдавать все счета пользователя. На вход логин пользователя, на выход все счета пользователя.
 */

