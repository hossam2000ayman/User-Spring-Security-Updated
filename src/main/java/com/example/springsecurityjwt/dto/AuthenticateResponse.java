package com.example.springsecurityjwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticateResponse(@JsonProperty(value = "token") String jwtToken) {
}
