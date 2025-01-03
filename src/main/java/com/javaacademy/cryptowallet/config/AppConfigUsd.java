package com.javaacademy.cryptowallet.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.service")
@Setter
@Getter
public class AppConfigUsd {
    private String url;
    private String header;
    private String token;
}
