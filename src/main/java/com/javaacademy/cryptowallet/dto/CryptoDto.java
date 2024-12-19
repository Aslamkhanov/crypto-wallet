package com.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaacademy.cryptowallet.crypto.CryptoCurrency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoDto {
    @JsonProperty("username")
    private String userName;
    @JsonProperty("crypto_type")
    private CryptoCurrency cryptoType;
}
