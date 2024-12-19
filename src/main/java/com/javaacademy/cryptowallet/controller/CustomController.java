package com.javaacademy.cryptowallet.controller;

import com.javaacademy.cryptowallet.dto.ResetPasswordRequest;
import com.javaacademy.cryptowallet.entity.User;
import com.javaacademy.cryptowallet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cryptowallet")
public class CustomController {
    private final UserService userService;
    @PostMapping("/user/signup")
    public void registersNewUser(@RequestBody User user) {
        userService.saveNewUser(user);
    }
    @PatchMapping("/user/reset-password")
    public void resetsPassword(@RequestBody ResetPasswordRequest request) {
        userService.resetUsersPassword(request.getLogin(),
                request.getOldPassword(),
                request.getNewPassword());
    }
}
/**
 * 5.1. По адресу POST /user/signup - регистрирует нового пользователя.
 * Будет передаваться json с полями: login, email, password.
 *
 * 5.2. По адресу POST /user/reset-password - сбрасывает пароль для существующего пользователя.
 * На вход json с полями: login, old_password, new_password.
 * Проверить работу через insomnia.
 */
