package com.javaacademy.cryptowallet.service.interfaces;

import java.math.BigDecimal;

public interface RublesService {
    BigDecimal convertDollarsToRuble(BigDecimal dollars);

    BigDecimal convertRublesToDollar(BigDecimal rubles);
}
