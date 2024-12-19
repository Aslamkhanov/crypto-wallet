package com.javaacademy.cryptowallet.service;

import com.javaacademy.cryptowallet.crypto.CryptoAccount;
import com.javaacademy.cryptowallet.crypto.CryptoCurrency;
import com.javaacademy.cryptowallet.storage.CryptoAccountStorage;
import com.javaacademy.cryptowallet.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Сервис умеет:
 * 4.3.3 Сохраняем в хранилище счетов.
 */

@Service
@RequiredArgsConstructor
public class CryptoAccountService {
    private final CryptoAccountStorage accountStorage;
    private final UserStorage userStorage;

    public CryptoAccount getAccount(UUID uuid) {
        if (!accountStorage.getData().containsKey(uuid)) {
            throw new RuntimeException("Такого счета нет: " + uuid);
        }
       return accountStorage.getAccount(uuid);
    }
    public List<CryptoAccount> getAllAccounts(String login) {
        List<CryptoAccount> accounts = new ArrayList<>();
        for (CryptoAccount account : accountStorage.getData().values()) {
            if (!account.getLogin().equals(login)) {
                throw new RuntimeException("Счета с таким логином нет: " + login);
            } else {
                accounts.add(account);
            }
        }
        return accounts;
    }
    public CryptoAccount createCryptoAccountUser(String login, CryptoCurrency cryptoCurrency) {
        if (!userStorage.getStorage().containsKey(login)) {
            throw new RuntimeException("Такого пользователя нет: " + login);
        }
        CryptoAccount account = new CryptoAccount(
                login,
                cryptoCurrency,
                BigDecimal.ZERO,
                UUID.randomUUID());
        accountStorage.saveAccount(account);
        return account;
    }
}
