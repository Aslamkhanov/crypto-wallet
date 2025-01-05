package com.javaacademy.cryptowallet.repository;

import com.javaacademy.cryptowallet.entity.CryptoAccount;
import com.javaacademy.cryptowallet.storage.CryptoAccountStorage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Data
@Component
@RequiredArgsConstructor
public class CryptoRepository {
    private final CryptoAccountStorage accountStorage;
    public void saveAccount(CryptoAccount account) {
        if (accountStorage.getData().containsKey(account.getUniqueAccountNumber())) {
            throw new RuntimeException("Такой счет уже есть: " + account.getUniqueAccountNumber());
        }
       accountStorage.getData().put(account.getUniqueAccountNumber(), account);
    }

    public CryptoAccount getAccount(UUID uuid) {
        return accountStorage.getData().get(uuid);
    }

    public List<CryptoAccount> getAllAccount(String login) {
        return accountStorage.getData().values().stream()
                .filter(account -> Objects.equals(account.getLogin(), login))
                .toList();
    }
}
