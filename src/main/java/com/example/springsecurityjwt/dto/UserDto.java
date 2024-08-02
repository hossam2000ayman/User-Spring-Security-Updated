package com.example.springsecurityjwt.dto;

import com.example.springsecurityjwt.entity.User;

public record UserDto(String name, String username, String email, String password) {

    public static User mapToEntity(UserDto userDto) {
        return new User(userDto.name, userDto.username(), userDto.email, userDto.password);
    }
}
