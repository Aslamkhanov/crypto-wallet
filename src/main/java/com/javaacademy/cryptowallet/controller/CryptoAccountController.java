package com.javaacademy.cryptowallet.controller;

import com.javaacademy.cryptowallet.crypto.CryptoAccount;
import com.javaacademy.cryptowallet.crypto.CryptoCurrencyType;
import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.dto.CryptoCreateDto;
import com.javaacademy.cryptowallet.dto.ExceptionResponseDto;
import com.javaacademy.cryptowallet.dto.ReplenishesAccountDto;
import com.javaacademy.cryptowallet.service.CryptoAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public BigDecimal showsRubleEquivalentCryptoAccount(@PathVariable UUID id) {
        return service.getAccountBalanceInRubles(id);
    }

    @Operation(
            summary = "Получаем баланс в рублях, со всех крипто счетов",
            description = "Получаем общий баланс в рублях, " +
                    "всех крипто кошельков пользователя")
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
    public BigDecimal showsRubleEquivalentCryptoAccount(@PathVariable String userName) {
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
    public void replenishesAccount(@RequestBody ReplenishesAccountDto replenishesAccountDto) {
        service.buyCryptocurrencyForRubles(replenishesAccountDto.getId(),
                replenishesAccountDto.getRublesAmount());
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
    public void withdrawsRublesFromAccount(@RequestBody ReplenishesAccountDto replenishesAccountDto) {
        service.sellCryptocurrencyForRubles(replenishesAccountDto.getId(),
                replenishesAccountDto.getRublesAmount());
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
        String userName = cryptoCreateDto.getUserName();
        CryptoCurrencyType cryptoType = cryptoCreateDto.getCryptoType();
        CryptoAccount cryptoAccount = service.createCryptoAccountUser(userName, cryptoType);
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
    public List<CryptoAccountDto> getAllAccounts(@RequestParam String username) {
        return service.getAllAccounts(username);
    }
}
