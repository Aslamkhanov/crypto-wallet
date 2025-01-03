package com.javaacademy.cryptowallet.service;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Profile("local")
@Service
@Getter
public class LocalServiceObtainingValueCryptocurrenciesDollars {

    @Value("${app.converter.local-value}")
    private int localValue;

    public BigDecimal gettingValueCryptocurrencyDollars(String cryptoName) {
        return BigDecimal.valueOf(localValue);
    }
}
