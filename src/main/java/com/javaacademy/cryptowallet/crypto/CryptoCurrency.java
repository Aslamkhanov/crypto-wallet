package com.javaacademy.cryptowallet.crypto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CryptoCurrency {
    BTC("bitcoin"), ETH("ethereum"), SOL("solana");
    private final String name;
}
