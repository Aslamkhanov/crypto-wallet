package com.javaacademy.cryptowallet.service.interfaces;

import java.math.BigDecimal;

public interface ObtainingCryptocurrencyValuesInDollars {
    BigDecimal getCryptoValueInDollars(String cryptoName);
}
