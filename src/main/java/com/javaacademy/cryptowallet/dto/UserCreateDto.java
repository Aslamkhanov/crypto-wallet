package com.javaacademy.cryptowallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Дто для создания пользователя")
public class UserCreateDto {
    @Schema(description = "уникальный логин пользователя")
    private String login;

    @Schema(description = "электронная почта пользователя")
    private String email;

    @Schema(description = "пароль пользователя")
    private String password;
}
