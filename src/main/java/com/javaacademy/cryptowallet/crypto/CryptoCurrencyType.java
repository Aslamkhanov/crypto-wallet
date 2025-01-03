package com.javaacademy.cryptowallet.crypto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CryptoCurrencyType {
    BTC("bitcoin"), ETH("ethereum"), SOL("solana");
    private final String description;
}
