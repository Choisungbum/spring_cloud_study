package com.example.user_service.service;

import com.example.user_service.dto.UserDto;
import com.example.user_service.entity.UserEntity;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(String userId);
    Iterable<UserEntity> getUserByAll();
}
