package com.javaacademy.cryptowallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReplenishesAccountDto {
    private UUID account_id;
    private BigDecimal rubles_amount;
}
