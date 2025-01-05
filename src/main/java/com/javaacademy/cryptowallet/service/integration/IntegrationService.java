package com.javaacademy.cryptowallet.service.integration;

import com.javaacademy.cryptowallet.config.AppConfigRub;
import com.jayway.jsonpath.JsonPath;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class IntegrationService {
    private static final String RATES_USD = "$['rates'].['USD']";
    private final OkHttpClient client;
    private final AppConfigRub configRub;

    @SneakyThrows
    public BigDecimal convertDollarsToRuble(BigDecimal dollars) {
        BigDecimal rate = getUsdRate();
        return dollars.multiply(rate);
    }

    @SneakyThrows
    public BigDecimal convertRublesToDollar(BigDecimal rubles) {
        BigDecimal rate = getUsdRate();
        return rubles.divide(rate, 8, RoundingMode.HALF_UP);
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
