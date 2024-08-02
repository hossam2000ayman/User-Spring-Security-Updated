package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.dto.AuthenticateRequest;
import com.example.springsecurityjwt.dto.AuthenticateResponse;
import com.example.springsecurityjwt.dto.UserDto;
import com.example.springsecurityjwt.dto.VerifyEmailRequest;
import com.example.springsecurityjwt.entity.User;
import com.example.springsecurityjwt.entity.enums.Role;
import com.example.springsecurityjwt.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public String register(UserDto userDto) throws Exception {
        Optional<User> userOptional = userRepository.findUserByUsername(userDto.username());
        if (userOptional.isEmpty()) {
            User user = Optional.of(userDto).stream().map(UserDto::mapToEntity).findFirst().get();
            user.setRole(Role.USER);
            user.setPassword(passwordEncoder.encode(userDto.password()));
            userRepository.save(user);
            return "User Register Successfully";
        } else throw new Exception("User Already Exists with username :: " + userDto.username());
    }

    public String verifyAccount(VerifyEmailRequest request) throws Exception {
        User user = userRepository.findUserByEmail(request.email()).orElseThrow(() -> new EntityNotFoundException("No User exists with email :: " + request.email()));
        if (!user.isEnabled()) {
            user.setEnabled(true);
        } else {
            throw new Exception("User email is already verified");
        }
        userRepository.saveAndFlush(user);
        return user.getRole().name().equalsIgnoreCase("admin") ? "Admin Verify Successfully" : "User Verify Successfully";
    }

    public AuthenticateResponse login(AuthenticateRequest request) throws Exception {
        User user = userRepository.findUserByUsername(request.username()).orElseThrow(() -> new EntityNotFoundException("User not found with username :: " + request.username()));
        if (!user.isEnabled()) throw new Exception("You have to Verify your account first");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        String jwtToken = jwtService.generateEndlessToken(user);
        return new AuthenticateResponse(jwtToken);
    }
}

