package com.javaacademy.cryptowallet.service.interfaces;

import java.math.BigDecimal;

public interface DollarsService {
    BigDecimal getCryptoValueInDollars(String cryptoName);
}
