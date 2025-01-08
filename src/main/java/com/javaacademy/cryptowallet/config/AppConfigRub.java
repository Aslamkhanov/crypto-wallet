package com.javaacademy.cryptowallet.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.service.service-cb")
public class AppConfigRub {

    private String url;
}
