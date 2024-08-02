package com.example.springsecurityjwt.dto;

import jakarta.validation.constraints.NotNull;

public record AuthenticateRequest(@NotNull(message = "username must not be null") String username,
                                  @NotNull(message = "password must not be null") String password) {

}
