package com.javaacademy.cryptowallet.controller;

import com.javaacademy.cryptowallet.dto.ExceptionResponseDto;
import com.javaacademy.cryptowallet.dto.UpdatePasswordDto;
import com.javaacademy.cryptowallet.dto.UserCreateDto;
import com.javaacademy.cryptowallet.entity.User;
import com.javaacademy.cryptowallet.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "Контроллер для работы с пользователями",
        description = "Создает и изменяет данные пользователя")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Создает нового пользователя",
            description = "Сохраняет нового пользователя")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Пользователь создан"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Логин занят",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка на сервере",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponseDto.class)
                    )
            )
    }
    )

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreateDto registersNewUser(@RequestBody UserCreateDto userDto) {
        userService.saveNewUser(userDto);
        return userDto;
    }

    @Operation(
            summary = "Обновляет пароль пользователю",
            description = "Перезаписывает старый пароль на новый")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "202",
                    description = "Пароль успешно изменен"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный пароль",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка на сервере",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponseDto.class)
                    )
            )
    }
    )
    @PatchMapping("/reset-password")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void resetsPassword(@RequestBody UpdatePasswordDto request) {
        userService.resetUsersPassword(request);
    }
}
