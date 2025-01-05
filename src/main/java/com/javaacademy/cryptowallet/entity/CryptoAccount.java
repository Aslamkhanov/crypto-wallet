package com.javaacademy.cryptowallet.entity;

import com.javaacademy.cryptowallet.crypto.CryptoCurrencyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Представление для крипто кошелька ")
public class CryptoAccount {
    @Schema(description = "Логин крипто кошелька")
    private String login;

    @Schema(description = "Вид крипто валюты")
    private CryptoCurrencyType cryptoCurrencyType;

    @Schema(description = "Баланс крипто кошелька")
    private BigDecimal balance;

    @Schema(description = "уникальный номер крипто кошелька")
    private UUID uniqueAccountNumber;
}
