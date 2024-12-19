package com.javaacademy.cryptowallet.controller;

import com.javaacademy.cryptowallet.crypto.CryptoAccount;
import com.javaacademy.cryptowallet.crypto.CryptoCurrency;
import com.javaacademy.cryptowallet.dto.CryptoDto;
import com.javaacademy.cryptowallet.service.CryptoAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

import static com.javaacademy.cryptowallet.crypto.CryptoCurrency.BTC;
import static com.javaacademy.cryptowallet.crypto.CryptoCurrency.ETH;
import static com.javaacademy.cryptowallet.crypto.CryptoCurrency.SOL;

/**
 * 5.1 по адресу POST /cryptowallet создает счет.
 * Будет передаваться json с полями: username, crypto_type.
 * Ожидается, что в crypto_type должен находиться один из 3 типов криптовалют (пункт 1.).
 * Если значение другое, то мы должны выбросить ошибку.
 * Данный endpoint должен вызывать создание счета (пункт 4.3).
 * Возвращает сгенерированный UUID нового счета.
 * 5.2 по адресу GET /cryptowallet?username=[значение] -
 * мы должны будем получить список счетов пользователя (пункт 4.2).
 * Проверить работу через insomnia.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/cryptowallet")
public class CryptoAccountController {
    private final CryptoAccountService service;
    @PostMapping
    public UUID createAccount(@RequestBody CryptoDto cryptoDto) {
        String userName = cryptoDto.getUserName();
        CryptoCurrency cryptoType = cryptoDto.getCryptoType();

        if (cryptoType == null || !EnumSet.of(BTC, ETH, SOL).contains(cryptoType)) {
            throw new IllegalArgumentException("Такой криптовалюты нет: " + cryptoType);
        }
        CryptoAccount cryptoAccount = service.createCryptoAccountUser(userName, cryptoType);
       return cryptoAccount.getUniqueAccountNumber();
    }
    @GetMapping
    public List<CryptoAccount> getAllAccount(@RequestParam String username) {
       return service.getAllAccounts(username);
    }
}
