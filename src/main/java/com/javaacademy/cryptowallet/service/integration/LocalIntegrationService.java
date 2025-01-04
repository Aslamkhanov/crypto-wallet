package com.javaacademy.cryptowallet.service.integration;

import com.javaacademy.cryptowallet.config.AppConfigRub;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Profile("local")
public class LocalIntegrationService {
    private static final int SCALE_TEN = 10;
    @Value("${app.usd.fixedRate:100}")
    private BigDecimal fixedRate;

    public BigDecimal convertDollarsToRuble(BigDecimal dollars) {
        return dollars.multiply(fixedRate);
    }

    public BigDecimal convertRublesToDollar(BigDecimal rubles) {
        return rubles.divide(fixedRate, SCALE_TEN, RoundingMode.HALF_UP);
    }
}
