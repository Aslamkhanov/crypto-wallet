package com.javaacademy.cryptowallet;


import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.dto.CryptoCreateDto;
import com.javaacademy.cryptowallet.dto.ReplenishesAccountDto;
import com.javaacademy.cryptowallet.dto.UserCreateDto;
import com.javaacademy.cryptowallet.entity.CryptoAccount;
import com.javaacademy.cryptowallet.repository.CryptoRepository;
import com.javaacademy.cryptowallet.service.CryptoAccountService;
import com.javaacademy.cryptowallet.service.UserService;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.javaacademy.cryptowallet.crypto.CryptoCurrencyType.BTC;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CryptoAccountControllerTest {
    private static final BigDecimal BALANCE = BigDecimal.valueOf(2000);
    private static final BigDecimal ADD_BALANCE = BigDecimal.valueOf(200);
    private static final UUID UUID_CRYPTO_ACCOUNT = UUID.fromString("33244e1a-3a9c-11eb-adc1-0242ac120002");
    private static final String NAME_LOGIN = "AnbuMen";
    private static final String EMAIL = "anbumen@mail.ru";
    private static final String PASSWORD = "123qwerty";
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

    @BeforeEach
    public void createUser() {
        UserCreateDto userCreateDto = new UserCreateDto(
                NAME_LOGIN,
                EMAIL,
                PASSWORD);
        userService.saveNewUser(userCreateDto);
    }

    @PostConstruct
    public void setInit() {
        requestSpecification = new RequestSpecBuilder()
                .setPort(port)
                .setBasePath("/cryptowallet")
                .log(LogDetail.ALL)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    @DisplayName("Регистрация крипто кошелька пользователя")
    public void registersNewUserSuccess() {
        CryptoCreateDto cryptoDto = new CryptoCreateDto(
                NAME_LOGIN, BTC);
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
        CryptoCreateDto createDto = new CryptoCreateDto(
                NAME_LOGIN, BTC);
        cryptoService.createCryptoAccountUser(createDto);
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
        assertEquals(userName, accountDto.getLogin());
        assertNotNull(accountDto.getUniqueAccountNumber());
        assertEquals(BigDecimal.valueOf(0), accountDto.getBalance());
        assertEquals(BTC, accountDto.getCryptoCurrencyType());
    }

    @Test
    @DisplayName("Пополнение баланса крипто кошелька")
    public void replenishesAccountSuccess() {
        CryptoAccount cryptoAccount = new CryptoAccount(NAME_LOGIN, BTC, BALANCE, UUID_CRYPTO_ACCOUNT);
        cryptoRepository.saveAccount(cryptoAccount);
        ReplenishesAccountDto replenishesAccountDto = new ReplenishesAccountDto(UUID_CRYPTO_ACCOUNT, ADD_BALANCE);
        given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(replenishesAccountDto)
                .post("/refill")
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .body("account_id", equalTo(UUID_CRYPTO_ACCOUNT.toString()))
                .body("rubles_amount", equalTo(200));
    }

    @Test
    @DisplayName("Снятие рублей с крипто кошелька")
    public void withdrawsRublesFromAccountSuccess() {
        CryptoAccount cryptoAccount = new CryptoAccount(NAME_LOGIN, BTC, BALANCE, UUID_CRYPTO_ACCOUNT);
        cryptoRepository.saveAccount(cryptoAccount);
        ReplenishesAccountDto replenishesAccountDto = new ReplenishesAccountDto(UUID_CRYPTO_ACCOUNT, ADD_BALANCE);
        String response = given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(replenishesAccountDto)
                .post("/withdrawal")
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .asString();
        String expected = "Операция прошла успешно. Продано 2,0000000000 bitcoin.";
        assertEquals(expected,
                response);
    }

    @Test
    @DisplayName("Получить баланс крипто кошелька в рублях, по имени пользователя")
    public void showsRubleEquivalentCryptoAccountSuccess() {
        CryptoAccount cryptoAccount = new CryptoAccount(NAME_LOGIN, BTC, BALANCE, UUID_CRYPTO_ACCOUNT);
        cryptoRepository.saveAccount(cryptoAccount);
        String userName = "AnbuMen";
        BigDecimal userBalance = given(requestSpecification)
                .param("userName", userName)
                .get("/balance")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .extract()
                .as(BigDecimal.class);
        assertEquals(userBalance, new BigDecimal("200000.00000000"));
    }

    @Test
    @DisplayName("Получить баланс крипто кошелька в рублях, по id")
    public void showsRubleEquivalentCryptoAccountIdSuccess() {
        CryptoAccount cryptoAccount = new CryptoAccount("AnbuMen", BTC, BALANCE, UUID_CRYPTO_ACCOUNT);
        cryptoRepository.saveAccount(cryptoAccount);
        String userName = "AnbuMen";
        BigDecimal userBalance = given(requestSpecification)
                .pathParam("id", UUID_CRYPTO_ACCOUNT)
                .get("/balance/{id}")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .extract()
                .as(BigDecimal.class);
        assertEquals(userBalance, new BigDecimal("200000.00000000"));
    }
}
