package com.javaacademy.cryptowallet.crypto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Data
public class CryptoAccount {
    private String login;
    private CryptoCurrencyType cryptoCurrencyType;
    private BigDecimal balance;
    private UUID uniqueAccountNumber;
}
