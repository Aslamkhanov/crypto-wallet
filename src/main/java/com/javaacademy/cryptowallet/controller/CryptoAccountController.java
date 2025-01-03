package com.javaacademy.cryptowallet.controller;

import com.javaacademy.cryptowallet.crypto.CryptoAccount;
import com.javaacademy.cryptowallet.crypto.CryptoCurrencyType;
import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.dto.CryptoDto;
import com.javaacademy.cryptowallet.dto.ReplenishesAccountDto;
import com.javaacademy.cryptowallet.service.CryptoAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cryptowallet")
public class CryptoAccountController {
    private final CryptoAccountService service;

    @GetMapping("/balance/{id}")
    public BigDecimal showsRubleEquivalentCryptoAccount(@PathVariable UUID id) {
        return service.getAccountBalanceInRubles(id);
    }
    @GetMapping("/balance?username={login}")
    public BigDecimal showsRubleEquivalentCryptoAccount(@PathVariable String userName) {
        return service.getRubleEquivalentBalanceAllUserAccounts(userName);
    }

    @PostMapping("/refill")
    public void replenishesAccount(@RequestBody ReplenishesAccountDto replenishesAccountDto) {
        service.buyCryptocurrencyForRubles(replenishesAccountDto.getAccount_id(),
                replenishesAccountDto.getRubles_amount());
    }
    @PostMapping("/withdrawal")
    public void withdrawsRublesFromAccount(@RequestBody ReplenishesAccountDto replenishesAccountDto) {
        service.sellCryptocurrencyForRubles(replenishesAccountDto.getAccount_id(),
                replenishesAccountDto.getRubles_amount());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createAccount(@RequestBody CryptoDto cryptoDto) {
        String userName = cryptoDto.getUserName();
        CryptoCurrencyType cryptoType = cryptoDto.getCryptoType();
        CryptoAccount cryptoAccount = service.createCryptoAccountUser(userName, cryptoType);
        return cryptoAccount.getUniqueAccountNumber();
    }

    @GetMapping
    public List<CryptoAccountDto> getAllAccount(@RequestParam String username) {
        return service.getAllAccounts(username);
    }
}
