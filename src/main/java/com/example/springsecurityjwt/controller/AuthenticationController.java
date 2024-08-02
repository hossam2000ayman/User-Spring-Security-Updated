package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.dto.AuthenticateRequest;
import com.example.springsecurityjwt.dto.AuthenticateResponse;
import com.example.springsecurityjwt.dto.UserDto;
import com.example.springsecurityjwt.dto.VerifyEmailRequest;
import com.example.springsecurityjwt.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(userDto));
    }

    @PostMapping("verify")
    public ResponseEntity<String> verifyAccount(@RequestBody VerifyEmailRequest request) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.verifyAccount(request));
    }

    @PostMapping("login")
    public ResponseEntity<AuthenticateResponse> login(@RequestBody AuthenticateRequest request) throws Exception {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(authenticationService.login(request));
    }


}
