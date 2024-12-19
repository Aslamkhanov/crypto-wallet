package com.javaacademy.cryptowallet.crypto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;
@AllArgsConstructor
@Data
public class CryptoAccount {
    private String login;
    private CryptoCurrency cryptoCurrency;
    private BigDecimal balance;
    private UUID uniqueAccountNumber;
}
/**
 * логин владельца, криптовалюта счета,
 * сколько криптовалюты на счету,
 * уникальный номер счета UUID.
 */
