package com.javaacademy.cryptowallet;


import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.dto.CryptoCreateDto;
import com.javaacademy.cryptowallet.dto.ReplenishesAccountDto;
import com.javaacademy.cryptowallet.dto.UserCreateDto;
import com.javaacademy.cryptowallet.entity.CryptoAccount;
import com.javaacademy.cryptowallet.repository.CryptoRepository;
import com.javaacademy.cryptowallet.service.CryptoAccountService;
import com.javaacademy.cryptowallet.service.UserService;
import com.javaacademy.cryptowallet.storage.CryptoAccountStorage;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.javaacademy.cryptowallet.crypto.CryptoCurrencyType.BTC;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class CryptoAccountControllerTest {
    private static final BigDecimal ADD_BALANCE = BigDecimal.valueOf(2000);
    private ResponseSpecification responseSpecification;
    @Value("${server.port}")
    private int port;
    private RequestSpecification requestSpecification;
    @Autowired
    private UserService userService;
    @Autowired
    private CryptoAccountService cryptoService;
    @Autowired
    private CryptoRepository cryptoRepository;

    @PostConstruct
    public void init() {
        requestSpecification = new RequestSpecBuilder()
                .setPort(port)
                .setBasePath("/cryptowallet")
                .log(LogDetail.ALL)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
        UserCreateDto userCreateDto = new UserCreateDto(
                "AnbuMen",
                "anbumen@mail.ru",
                "123qwerty");
        userService.saveNewUser(userCreateDto);
        UUID uuid = UUID.fromString("33244e1a-3a9c-11eb-adc1-0242ac120002");
        CryptoAccount cryptoAccount = new CryptoAccount("AnbuMen", BTC, ADD_BALANCE, uuid);
        cryptoRepository.saveAccount(cryptoAccount);
//        CryptoCreateDto createDto = new CryptoCreateDto(
//                "AnbuMen", BTC);
//        cryptoService.createCryptoAccountUser(createDto);
    }

    @Test
    @DisplayName("Регистрация крипто кошелька пользователя")
    public void registersNewUserSuccess() {
        CryptoCreateDto cryptoDto = new CryptoCreateDto(
                "AnbuMen", BTC);

        UUID accountNumber = given(requestSpecification)
                .contentType("application/json")
                .body(cryptoDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(UUID.class);
        assertNotNull(accountNumber);
    }

    @Test
    @DisplayName("Получение всех крипто кошельков пользователя")
    public void getAllAccountsSuccess() {
        String userName = "AnbuMen";
        List<CryptoAccountDto> cryptoAccountDtos = given(requestSpecification)
                .param("userName", userName)
                .get()
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<>() {
                });
        assertEquals(1, cryptoAccountDtos.size());
        CryptoAccountDto accountDto = cryptoAccountDtos.get(0);
        assertEquals("AnbuMen", accountDto.getLogin());
        assertNotNull(accountDto.getUniqueAccountNumber());
        assertEquals(BigDecimal.valueOf(2000), accountDto.getBalance());
        assertEquals(BTC, accountDto.getCryptoCurrencyType());
    }

    @Test
    @DisplayName("Пополнение баланса крипто кошелька")
    public void replenishesAccountSuccess() {
        UUID uuid = UUID.fromString("33244e1a-3a9c-11eb-adc1-0242ac120002");
        ReplenishesAccountDto replenishesAccountDto = new ReplenishesAccountDto(uuid, BigDecimal.valueOf(200)
        );
        given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(replenishesAccountDto)
                .post("/refill")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.ACCEPTED.value());
    }
}
