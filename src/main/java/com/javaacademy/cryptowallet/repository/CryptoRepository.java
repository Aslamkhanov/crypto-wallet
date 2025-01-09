package com.javaacademy.cryptowallet.repository;

import com.javaacademy.cryptowallet.entity.CryptoAccount;
import com.javaacademy.cryptowallet.storage.CryptoAccountStorage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@Data
public class CryptoRepository {
    private final CryptoAccountStorage accountStorage;

    public void saveAccount(CryptoAccount account) {
        if (accountStorage.getCryptoData().containsKey(account.getUniqueAccountNumber())) {
            throw new RuntimeException("Такой счет уже есть: " + account.getUniqueAccountNumber());
        }
        accountStorage.getCryptoData().put(account.getUniqueAccountNumber(), account);
        log.info("Крипто кошелек пользователя: {} успешно сохранен", account.getUserLogin());
    }

    public CryptoAccount getAccount(UUID uuid) {
            return accountStorage.getCryptoData().get(uuid);
    }

    public List<CryptoAccount> getAllAccount(String login) {
        return accountStorage.getCryptoData().values().stream()
                .filter(account -> Objects.equals(account.getUserLogin(), login))
                .collect(Collectors.toList());
    }
}
