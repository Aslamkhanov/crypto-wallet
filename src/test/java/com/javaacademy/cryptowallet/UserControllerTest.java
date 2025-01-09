package com.javaacademy.cryptowallet;

import com.javaacademy.cryptowallet.dto.UpdatePasswordDto;
import com.javaacademy.cryptowallet.dto.UserCreateDto;
import com.javaacademy.cryptowallet.service.UserService;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {
    private static final String LOGIN = "Anbu";
    private static final String EMAIL = "anbumen@mail.ru";
    private static final String PASSWORD = "123qwerty";
    private ResponseSpecification responseSpecification;
    @Value("${server.port}")
    private int port;
    private RequestSpecification requestSpecification;
    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
        requestSpecification = new RequestSpecBuilder()
                .setPort(port)
                .setBasePath("/user")
                .log(LogDetail.ALL)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    @DisplayName("Регистрация пользователя")
    public void registersNewUserSuccess() {
        UserCreateDto userCreateDto = new UserCreateDto(
                LOGIN,
                EMAIL,
                PASSWORD);
        given(requestSpecification)
                .contentType("application/json")
                .body(userCreateDto)
                .post("/signup")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.CREATED.value())
                .body("login", equalTo(LOGIN))
                .body("email", equalTo(EMAIL))
                .body("password", equalTo(PASSWORD));
    }

    @Test
    @DisplayName("Обновление пароля пользователя")
    public void updateTeacherSuccess() {
        UserCreateDto userCreateDto = new UserCreateDto(
                "Ivan",
                "ivan@mail.ru",
                "123qwerty");
        userService.saveNewUser(userCreateDto);
        UpdatePasswordDto passwordDto = new UpdatePasswordDto(
                "Ivan",
                "123qwerty",
                "qwerty321");

        given(requestSpecification)
                .contentType("application/json")
                .body(passwordDto)
                .patch("/reset-password")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.ACCEPTED.value());
    }
}
