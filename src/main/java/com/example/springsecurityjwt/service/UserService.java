package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.dto.UserDto;
import com.example.springsecurityjwt.entity.User;
import com.example.springsecurityjwt.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public User updateUser(Long id, UserDto newUserDto) {
        User oldUser = getUser(id);
        if (newUserDto.name() != null) oldUser.setName(newUserDto.name());
        if (newUserDto.username() != null) if (userRepository.findUserByUsername(newUserDto.username()).isEmpty())
            oldUser.setUsername(newUserDto.username());
        if (newUserDto.email() != null)
            if (userRepository.findUserByEmail(newUserDto.email()).isEmpty()) oldUser.setEmail(newUserDto.email());

        if (newUserDto.password() != null) oldUser.setPassword(passwordEncoder.encode(newUserDto.password()));
        return userRepository.saveAndFlush(oldUser);

    }


    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }


}
