package com.javaacademy.cryptowallet.service.integration;

import com.javaacademy.cryptowallet.config.AppConfigRub;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Profile("local")
public class LocalIntegrationService {
    private final AppConfigRub configRub;

    public BigDecimal convertDollarsToRuble(BigDecimal dollars) {
        BigDecimal fixedRate = BigDecimal.valueOf(configRub.getFixedRate());
        return dollars.multiply(fixedRate);
    }

    public BigDecimal convertRublesToDollar(BigDecimal rubles) {
        BigDecimal fixedRate = BigDecimal.valueOf(configRub.getFixedRate());
        return rubles.divide(fixedRate, 9, RoundingMode.HALF_UP);
    }
}
