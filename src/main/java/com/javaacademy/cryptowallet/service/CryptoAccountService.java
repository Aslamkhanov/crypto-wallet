package com.javaacademy.cryptowallet.service;

import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.dto.CryptoCreateDto;
import com.javaacademy.cryptowallet.dto.ReplenishesAccountDto;
import com.javaacademy.cryptowallet.entity.CryptoAccount;
import com.javaacademy.cryptowallet.exeption.ResourceNotFoundException;
import com.javaacademy.cryptowallet.mapper.CryptoAccountMapper;
import com.javaacademy.cryptowallet.repository.CryptoRepository;
import com.javaacademy.cryptowallet.repository.UserRepository;
import com.javaacademy.cryptowallet.service.integration.IntegrationService;
import com.javaacademy.cryptowallet.service.interfaces.ObtainingCryptocurrencyValuesInDollars;
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
    private final CryptoRepository cryptoRepository;
    private final UserRepository userRepository;
    private final CryptoAccountMapper accountMapper;
    private final ObtainingCryptocurrencyValuesInDollars cryptocurrenciesDollars;
    private final IntegrationService integrationService;

    public CryptoAccount getAccount(UUID uuid) {
        if (!cryptoRepository.getAccountStorage().getData().containsKey(uuid)) {
            throw new RuntimeException("Такого счета нет: " + uuid);
        }
        return cryptoRepository.getAccount(uuid);
    }

    public String sellCryptocurrencyForRubles(ReplenishesAccountDto accountDto) {
        log.info("Снять со счета {} сумму {} рублей", accountDto.getId(), accountDto.getRublesAmount());

        CryptoAccount cryptoAccount = getAccount(accountDto.getId());
        String cryptoTypeName = cryptoAccount.getCryptoCurrencyType().getDescription();
        BigDecimal cryptocurrencyDollars = cryptocurrenciesDollars.getCryptoValueInDollars(cryptoTypeName);
        BigDecimal dollars = integrationService.convertRublesToDollar(accountDto.getRublesAmount());

        BigDecimal cryptoToSell = dollars.divide(cryptocurrencyDollars, 8, RoundingMode.HALF_UP);

        if (cryptoAccount.getBalance().compareTo(cryptoToSell) < 0) {
            throw new IllegalArgumentException("Недостаточно криптовалюты на счету");
        }
        cryptoAccount.setBalance(cryptoAccount.getBalance().subtract(cryptoToSell));
        return String.format("Операция прошла успешно. Продано %.10f %s.", cryptoToSell, cryptoTypeName);
    }

    public BigDecimal getRubleEquivalentBalanceAllUserAccounts(String userName) {
        log.info("Получение рублевого эквивалента всех счетов пользователя {}", userName);
        List<CryptoAccount> userAccounts = cryptoRepository.getAccountStorage().getData()
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

    public void buyCryptocurrencyForRubles(ReplenishesAccountDto accountDto) {
        log.info("Пополнение счета {} на сумму {} рублей", accountDto.getId(), accountDto.getRublesAmount());
        CryptoAccount cryptoAccount = getAccount(accountDto.getId());
        String cryptoTypeName = cryptoAccount.getCryptoCurrencyType().getDescription();

        BigDecimal cryptocurrencyDollars = cryptocurrenciesDollars.getCryptoValueInDollars(cryptoTypeName);
        BigDecimal dollars = integrationService.convertRublesToDollar(accountDto.getRublesAmount());

        BigDecimal totalSum = dollars.divide(cryptocurrencyDollars, 8, RoundingMode.HALF_UP);

        log.info("Добавление {} {} к балансу", totalSum, cryptoTypeName);
        cryptoAccount.setBalance(cryptoAccount.getBalance().add(totalSum));

        log.info("Обновленный баланс счета {}: {}", accountDto.getId(), cryptoAccount.getBalance());
    }

    public List<CryptoAccountDto> getAllAccounts(String login) throws ResourceNotFoundException {
        List<CryptoAccountDto> accountDto = cryptoRepository.getAccountStorage().getData()
                .values().stream()
                .filter(account -> Objects.equals(account.getLogin(), login))
                .map(accountMapper::convertCryptoAccountDto)
                .collect(Collectors.toList());
        if (accountDto.isEmpty()) {
            throw new ResourceNotFoundException("Счета с таким логином нет: " + login);
        }
        return accountDto;
    }

    public CryptoAccount createCryptoAccountUser(CryptoCreateDto cryptoCreateDto) {
        if (!userRepository.getUserStorage().getData().containsKey(cryptoCreateDto.getUserName())) {
            throw new RuntimeException("Такого пользователя нет: " + cryptoCreateDto.getUserName());
        }
        CryptoAccount account = new CryptoAccount(
                cryptoCreateDto.getUserName(),
                cryptoCreateDto.getCryptoType(),
                BigDecimal.ZERO,
                UUID.randomUUID());
        cryptoRepository.saveAccount(account);
        return account;
    }
}
