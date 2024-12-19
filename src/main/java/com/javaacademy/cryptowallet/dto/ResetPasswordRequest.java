package com.javaacademy.cryptowallet.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class ResetPasswordRequest {
    @NonNull
    private String login;
    @NonNull
    private String oldPassword;
    @NonNull
    private String newPassword;
}
