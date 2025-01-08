package com.javaacademy.cryptowallet.service.local;


import com.javaacademy.cryptowallet.service.interfaces.DollarsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Profile("local")
@Service
public class LocalDollarsConverterServiceService implements DollarsService {
    @Value("${app.converter.local-value}")
    private BigDecimal localValue;

    @Override
    public BigDecimal getCryptoValueInDollars(String cryptoName) {
        return localValue;
    }
}
