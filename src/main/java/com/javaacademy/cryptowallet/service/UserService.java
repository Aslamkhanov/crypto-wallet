package com.javaacademy.cryptowallet.service;

import com.javaacademy.cryptowallet.dto.UpdatePasswordDto;
import com.javaacademy.cryptowallet.dto.UserCreateDto;
import com.javaacademy.cryptowallet.entity.User;
import com.javaacademy.cryptowallet.mapper.UserMapper;
import com.javaacademy.cryptowallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void saveNewUser(UserCreateDto userDto) {
        User user = userMapper.convertUser(userDto);
        userRepository.saveUser(user);
    }

    public User getUserByLogin(String login) {
        return userRepository.getUser(login)
                .orElseThrow(() -> new RuntimeException("Пользователь с логином "
                        + login + " не найден"));
    }

    public void resetUsersPassword(UpdatePasswordDto passwordDto) {
        User user = getUserByLogin(passwordDto.getLogin());
        if (!user.getPassword().equals(passwordDto.getOldPassword())) {
            throw new RuntimeException("Неправильный пароль");
        }
        user.setPassword(passwordDto.getNewPassword());
        log.info("Пароль изменен!");
    }
}
