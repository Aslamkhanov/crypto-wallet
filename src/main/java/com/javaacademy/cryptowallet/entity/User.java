package com.javaacademy.cryptowallet.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Представления для создания пользователя")
public class User {

    @Schema(description = "уникальный логин пользователя")
    private String login;

    @Schema(description = "электронная почта пользователя")
    private String email;

    @Schema(description = "пароль пользователя")
    private String password;
}
