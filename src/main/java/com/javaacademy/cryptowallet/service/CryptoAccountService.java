package com.javaacademy.cryptowallet.service;

import com.javaacademy.cryptowallet.crypto.CryptoAccount;
import com.javaacademy.cryptowallet.crypto.CryptoCurrencyType;
import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.mapper.CryptoAccountMapper;
import com.javaacademy.cryptowallet.service.integration.IntegrationService;
import com.javaacademy.cryptowallet.service.interfaces.ObtainingCryptocurrencyValuesInDollars;
import com.javaacademy.cryptowallet.storage.CryptoAccountStorage;
import com.javaacademy.cryptowallet.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CryptoAccountService {
    private final CryptoAccountStorage accountStorage;
    private final UserStorage userStorage;
    private final CryptoAccountMapper accountMapper;
    private final ObtainingCryptocurrencyValuesInDollars cryptocurrenciesDollars;
    private final IntegrationService integrationService;

    public CryptoAccount getAccount(UUID uuid) {
        if (!accountStorage.getData().containsKey(uuid)) {
            throw new RuntimeException("Такого счета нет: " + uuid);
        }
        return accountStorage.getAccount(uuid);
    }

    public String sellCryptocurrencyForRubles(UUID uuid, BigDecimal withdrawRubles) {
        log.info("Снять со счета {} сумму {} рублей", uuid, withdrawRubles);

        CryptoAccount cryptoAccount = getAccount(uuid);
        String cryptoTypeName = cryptoAccount.getCryptoCurrencyType().getDescription();
        BigDecimal cryptocurrencyDollars = cryptocurrenciesDollars.getCryptoValueInDollars(cryptoTypeName);
        BigDecimal dollars = integrationService.convertRublesToDollar(withdrawRubles);

        BigDecimal cryptoToSell = dollars.divide(cryptocurrencyDollars, 10, RoundingMode.HALF_UP);

        if (cryptoAccount.getBalance().compareTo(cryptoToSell) < 0) {
            throw new IllegalArgumentException("Недостаточно криптовалюты на счету");
        }
        cryptoAccount.setBalance(cryptoAccount.getBalance().subtract(cryptoToSell));
        return String.format("Операция прошла успешно. Продано %.10f %s.", cryptoToSell, cryptoTypeName);
    }

    public BigDecimal getRubleEquivalentBalanceAllUserAccounts(String userName) {
        log.info("Получение рублевого эквивалента всех счетов пользователя {}", userName);
        List<CryptoAccount> userAccounts = accountStorage.getData()
                .values().stream()
                .filter(account -> Objects.equals(account.getLogin(), userName))
                .toList();

        if (userAccounts.isEmpty()) {
            throw new RuntimeException("У пользователя " + userName + " нет счетов");
        }

        BigDecimal totalRubleEquivalent = userAccounts.stream()
                .map(account -> {
                    String cryptoTypeName = account.getCryptoCurrencyType().getDescription();
                    BigDecimal cryptocurrencyDollars = cryptocurrenciesDollars.getCryptoValueInDollars(cryptoTypeName);
                    BigDecimal balanceInDollars = account.getBalance().multiply(cryptocurrencyDollars);
                    return integrationService.convertDollarsToRuble(balanceInDollars);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("Рублевый эквивалент всех счетов пользователя {}: {}", userName, totalRubleEquivalent);
        return totalRubleEquivalent;
    }

    public BigDecimal getAccountBalanceInRubles(UUID uuid) {
        CryptoAccount cryptoAccount = getAccount(uuid);
        String cryptoTypeName = cryptoAccount.getCryptoCurrencyType().getDescription();

        BigDecimal cryptocurrencyDollars = cryptocurrenciesDollars.getCryptoValueInDollars(cryptoTypeName);
        BigDecimal balanceInDollars = cryptoAccount.getBalance().multiply(cryptocurrencyDollars);

        return integrationService.convertDollarsToRuble(balanceInDollars);
    }

    public void buyCryptocurrencyForRubles(UUID uuid, BigDecimal sumRubles) {
        log.info("Пополнение счета {} на сумму {} рублей", uuid, sumRubles);

        CryptoAccount cryptoAccount = getAccount(uuid);
        String cryptoTypeName = cryptoAccount.getCryptoCurrencyType().getDescription();

        BigDecimal cryptocurrencyDollars = cryptocurrenciesDollars.getCryptoValueInDollars(cryptoTypeName);
        BigDecimal dollars = integrationService.convertRublesToDollar(sumRubles);

        BigDecimal totalSum = dollars.divide(cryptocurrencyDollars, 10, RoundingMode.HALF_UP);

        log.info("Добавление {} {} к балансу", totalSum, cryptoTypeName);
        cryptoAccount.setBalance(cryptoAccount.getBalance().add(totalSum));

        log.info("Обновленный баланс счета {}: {}", uuid, cryptoAccount.getBalance());
    }

    public List<CryptoAccountDto> getAllAccounts(String login) {
        List<CryptoAccountDto> accountDto = accountStorage.getData()
                .values().stream()
                .filter(account -> Objects.equals(account, login))
                .filter(Objects::nonNull)
                .map(accountMapper::convertCryptoAccountDto)
                .collect(Collectors.toList());
        if (accountDto.isEmpty()) {
            throw new RuntimeException("Счета с таким логином нет: " + login);
        }
        return accountDto;
    }

    public CryptoAccount createCryptoAccountUser(String login, CryptoCurrencyType cryptoCurrencyType) {
        if (!userStorage.getStorage().containsKey(login)) {
            throw new RuntimeException("Такого пользователя нет: " + login);
        }
        CryptoAccount account = new CryptoAccount(
                login,
                cryptoCurrencyType,
                BigDecimal.ZERO,
                UUID.randomUUID());
        accountStorage.saveAccount(account);
        return account;
    }
}
