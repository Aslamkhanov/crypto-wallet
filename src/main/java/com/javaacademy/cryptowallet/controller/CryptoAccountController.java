package com.javaacademy.cryptowallet.controller;

import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.dto.CryptoCreateDto;
import com.javaacademy.cryptowallet.dto.ExceptionResponseDto;
import com.javaacademy.cryptowallet.dto.ReplenishesAccountDto;
import com.javaacademy.cryptowallet.entity.CryptoAccount;
import com.javaacademy.cryptowallet.exeption.ResourceNotFoundException;
import com.javaacademy.cryptowallet.service.CryptoAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cryptowallet")
@Tag(name = "Контроллер для работы с крипто кошельками",
        description = "Содержит методы для работы с крипто кошельками")
public class CryptoAccountController {
    private final CryptoAccountService service;

    @Operation(
            summary = "Получаем баланс в рублях",
            description = "Получаем баланс крипто кошелька в рублях")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "баланс крипто кошелька в рублях"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "крипто кошелек не найден",
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
    @GetMapping("/balance/{id}")
    public BigDecimal showsRubleEquivalentCryptoAccountId(@PathVariable UUID id) {
        return service.getAccountBalanceInRubles(id);
    }

    @Operation(
            summary = "Получаем баланс в рублях, со всех крипто счетов",
            description = "Получаем общий баланс в рублях, "
                   + "всех крипто кошельков пользователя")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "баланс всех крипто кошельков в рублях"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "крипто кошельки не найдены",
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
    @GetMapping("/balance")
    public BigDecimal showsRubleEquivalentCryptoAccount(@RequestParam String userName) {
        return service.getRubleEquivalentBalanceAllUserAccounts(userName);
    }

    @Operation(
            summary = "Пополнение баланса в рублях",
            description = "Пополнение крипто кошелька в рублях")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "202",
                    description = "Пополнение в рублях, успешно"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "крипто кошелек не найден",
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
    @PostMapping("/refill")
    public ReplenishesAccountDto replenishesAccount(@RequestBody ReplenishesAccountDto replenishesAccountDto) {
        service.buyCryptocurrencyForRubles(replenishesAccountDto);
        return replenishesAccountDto;
    }

    @Operation(
            summary = "Списание с крипто счета в рублях",
            description = "Списание с баланса крипто счета в рублях")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "202",
                    description = "Списание в рублях, успешно"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "крипто кошелек не найден",
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
    @PostMapping("/withdrawal")
    public String withdrawsRublesFromAccount(@RequestBody ReplenishesAccountDto replenishesAccountDto) {
        return service.sellCryptocurrencyForRubles(replenishesAccountDto);
    }

    @Operation(
            summary = "Создание крипто кошелька",
            description = "Создание нового крипто кошелька")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Крипто кошелек успешно создан"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка",
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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createCryptoAccount(@RequestBody CryptoCreateDto cryptoCreateDto) {
        CryptoAccount cryptoAccount = service.createCryptoAccountUser(cryptoCreateDto);
        return cryptoAccount.getUniqueAccountNumber();
    }

    @Operation(
            summary = "Возвращает крипто кошельки пользователя",
            description = "Возвращает все крипто кошельки пользователя")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Крипто кошельки найдены"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Крипто кошельки не найдены",
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
    @GetMapping
    public List<CryptoAccountDto> getAllAccounts(@RequestParam String userName) {
        try {
            return service.getAllAccounts(userName);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
