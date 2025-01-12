package com.javaacademy.cryptowallet.service.local;

import com.javaacademy.cryptowallet.service.interfaces.RublesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Profile("local")
public class LocalIRubleConverterService implements RublesService {
    private static final int SCALE_EIGHT = 8;

    @Value("${app.usd.fixedRate}")
    private BigDecimal fixedRate;

    @Override
    public BigDecimal convertDollarsToRuble(BigDecimal dollars) {
        return dollars.divide(fixedRate, SCALE_EIGHT, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal convertRublesToDollar(BigDecimal rubles) {
        return rubles.multiply(fixedRate);
    }
}
