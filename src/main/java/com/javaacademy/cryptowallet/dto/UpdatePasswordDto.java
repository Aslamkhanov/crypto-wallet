package com.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Дто для обновления пароля")
public class UpdatePasswordDto {
    @NonNull
    @Schema(description = "логин пользователя")
    private String login;

    @JsonProperty("old_password")
    @NonNull
    @Schema(description = "старый пароль")
    private String oldPassword;

    @JsonProperty("new_password")
    @NonNull
    @Schema(description = "новый пароль")
    private String newPassword;
}
