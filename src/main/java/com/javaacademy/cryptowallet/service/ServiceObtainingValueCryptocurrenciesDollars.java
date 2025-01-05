package com.javaacademy.cryptowallet.service;

import com.javaacademy.cryptowallet.config.AppConfigUsd;
import com.javaacademy.cryptowallet.service.interfaces.ObtainingCryptocurrencyValuesInDollars;
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

@Service
@RequiredArgsConstructor
@Profile("prod")
public class ServiceObtainingValueCryptocurrenciesDollars implements ObtainingCryptocurrencyValuesInDollars {
    private static final String FORMAT_USD = "$['%s']['usd']";
    private static final String FORMAT_URL = "%s/simple/price?ids=%s&vs_currencies=usd";
    private final OkHttpClient client;
    private final AppConfigUsd appConfigUsd;

    @SneakyThrows
    @Override
    public BigDecimal getCryptoValueInDollars(String cryptoName) {
        Request request = getRequest(cryptoName);
        return getResponseUsd(request, cryptoName);
    }

    private Request getRequest(String cryptoName) {
        String url = String.format(FORMAT_URL,
                appConfigUsd.getUrl(), cryptoName);
        return new Request.Builder()
                .get()
                .url(url)
                .addHeader(appConfigUsd.getHeader(), appConfigUsd.getToken())
                .build();
    }

    @SneakyThrows
    private BigDecimal getResponseUsd(Request request, String cryptoName) {
        @Cleanup Response response = client.newCall(request).execute();
        if (!response.isSuccessful() || response.body() == null) {
            throw new RuntimeException("Ошибка response: " + response);
        }
        String jsonResponse = response.body().string();
        String jsonPath = String.format(FORMAT_USD, cryptoName);
        return JsonPath.parse(jsonResponse).read(jsonPath, BigDecimal.class);
    }
}
