package com.javaacademy.cryptowallet.service.local;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Profile("local")
public class LocalIRubleConverterService {
    private static final int EIGHT = 8;

    @Value("${app.usd.fixedRate}")
    private BigDecimal fixedRate;

    public BigDecimal convertDollarsToRuble(BigDecimal dollars) {
        return dollars.divide(fixedRate, EIGHT, RoundingMode.HALF_UP);
    }

    public BigDecimal convertRublesToDollar(BigDecimal rubles) {
        return rubles.multiply(fixedRate);
    }
}
