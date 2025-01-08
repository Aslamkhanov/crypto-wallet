package com.javaacademy.cryptowallet.mapper;

import com.javaacademy.cryptowallet.dto.UserCreateDto;
import com.javaacademy.cryptowallet.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User convertUser(UserCreateDto userCreateDto) {
        return new User(userCreateDto.getLogin(),
                userCreateDto.getEmail(),
                userCreateDto.getPassword());
    }
}
