package com.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Дто для изменения баланса кошелька")
public class ReplenishesAccountDto {
    @Schema(description = "уникальный номер кошелька")
    @JsonProperty("account_id")
    private UUID id;

    @Schema(description = "сумма операции в рублях")
    @JsonProperty("rubles_amount")
    private BigDecimal rublesAmount;
}
