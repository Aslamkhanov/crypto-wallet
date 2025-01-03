package com.javaacademy.cryptowallet.dto;

import com.javaacademy.cryptowallet.crypto.CryptoCurrencyType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;
@AllArgsConstructor
@Data
public class CryptoAccountDto {
    private String login;
    private CryptoCurrencyType cryptoCurrencyType;
    private BigDecimal balance;
    private UUID uniqueAccountNumber;
}
