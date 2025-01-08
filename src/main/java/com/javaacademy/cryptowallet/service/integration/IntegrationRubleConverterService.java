package com.javaacademy.cryptowallet.service.integration;

import com.javaacademy.cryptowallet.config.AppConfigRub;
import com.javaacademy.cryptowallet.service.interfaces.RublesService;
import com.jayway.jsonpath.JsonPath;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Profile("prod")
public class IntegrationRubleConverterService implements RublesService {
    private static final String RATES_USD = "$['rates'].['USD']";
    private final OkHttpClient client;
    private final AppConfigRub configRub;
    private static final int EIGHT = 8;
    @Override
    @SneakyThrows
    public BigDecimal convertDollarsToRuble(BigDecimal dollars) {
        BigDecimal rate = getUsdRate();
        return dollars.divide(rate, EIGHT, RoundingMode.HALF_UP);
    }
    @Override
    @SneakyThrows
    public BigDecimal convertRublesToDollar(BigDecimal rubles) {
        BigDecimal rate = getUsdRate();
        return rubles.multiply(rate);
    }

    public BigDecimal getUsdRate() {
        Request requestRate = getRequestRate();
        return getResponse(requestRate);
    }

    private Request getRequestRate() {
        return new Request.Builder()
                .get()
                .url(configRub.getUrl())
                .build();
    }

    @SneakyThrows
    private BigDecimal getResponse(Request requestRate) {
        @Cleanup Response response = client.newCall(requestRate).execute();
        if (response.isSuccessful() && response.body() != null) {
            String responseBody = response.body().string();
            return JsonPath.parse(responseBody)
                    .read(JsonPath.compile(RATES_USD),
                            BigDecimal.class);
        }
        throw new RuntimeException("Запрос не прошел");
    }
}
