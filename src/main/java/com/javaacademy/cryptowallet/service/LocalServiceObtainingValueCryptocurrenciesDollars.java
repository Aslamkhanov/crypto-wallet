package com.javaacademy.cryptowallet.service;


import com.javaacademy.cryptowallet.service.interfaces.ObtainingCryptocurrencyValuesInDollars;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Profile("local")
@Service
public class LocalServiceObtainingValueCryptocurrenciesDollars implements ObtainingCryptocurrencyValuesInDollars {

    //@Value("${app.converter.local-value}")
    @Value("${app.converter.local-value:10000}")
    private BigDecimal localValue;

    @Override
    public BigDecimal getCryptoValueInDollars(String cryptoName) {
        return localValue;
    }
}
