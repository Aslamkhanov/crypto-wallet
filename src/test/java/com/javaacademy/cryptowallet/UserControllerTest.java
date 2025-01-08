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
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {
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
        UserCreateDto userCreateDto = new UserCreateDto(
                "Ivan",
                "ivan@mail.ru",
                "123qwerty");
        userService.saveNewUser(userCreateDto);
    }


    @Test
    @DisplayName("Регистрация пользователя")
    public void registersNewUserSuccess() {
        UserCreateDto userCreateDto = new UserCreateDto(
                "Anbu",
                "anbu@mail.ru",
                "qwerty123");

        given(requestSpecification)
                .contentType("application/json")
                .body(userCreateDto)
                .post("/signup")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.CREATED.value())
                .body("login", equalTo("Anbu"))
                .body("email", equalTo("anbu@mail.ru"))
                .body("password", equalTo("qwerty123"));
    }

    @Test
    @DisplayName("Обновление пароля пользователя")
    public void updateTeacherSuccess() {
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
