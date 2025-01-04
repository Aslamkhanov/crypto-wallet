package com.javaacademy.cryptowallet.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenApi() {
        Info info = new Info()
                .title("Это api приложения \"Криптокошелек\"")
                .description("Приложение регистрирует пользователей и создает крипто кошельки."
                        + "Пополнять и списывать с баланса крипто кошельков, а также узнавать их баланс в рублях");
        return new OpenAPI().info(info);
    }
}
