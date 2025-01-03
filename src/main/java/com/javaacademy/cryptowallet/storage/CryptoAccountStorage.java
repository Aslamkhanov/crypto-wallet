package com.javaacademy.cryptowallet.storage;

import com.javaacademy.cryptowallet.crypto.CryptoAccount;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

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
        return data.values().stream()
                .filter(account -> Objects.equals(account, login))
                .toList();
    }
}
