package com.javaacademy.cryptowallet.dto;

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
@Schema(description = "Дто крипто кошелька")
public class CryptoAccountDto {
    @Schema(description = "логин пользователя")
    private String login;

    @Schema(description = "вид криптовалюты")
    private CryptoCurrencyType cryptoCurrencyType;

    @Schema(description = "баланс криптовалюты")
    private BigDecimal balance;

    @Schema(description = "уникальный аутентификатор пользователя")
    private UUID uniqueAccountNumber;
}
