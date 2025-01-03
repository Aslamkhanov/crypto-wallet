package com.javaacademy.cryptowallet.controller;

import com.javaacademy.cryptowallet.dto.ResetPasswordRequest;
import com.javaacademy.cryptowallet.entity.User;
import com.javaacademy.cryptowallet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cryptowallet")
public class CustomController {
    private final UserService userService;
    @PostMapping("/user/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void registersNewUser(@RequestBody User user) {
        userService.saveNewUser(user);
    }
    @PatchMapping("/user/reset-password")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void resetsPassword(@RequestBody ResetPasswordRequest request) {
        userService.resetUsersPassword(request.getLogin(),
                request.getOldPassword(),
                request.getNewPassword());
    }
}

