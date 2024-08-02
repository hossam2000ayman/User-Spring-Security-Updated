package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.dto.UserDto;
import com.example.springsecurityjwt.entity.User;
import com.example.springsecurityjwt.entity.enums.Role;
import com.example.springsecurityjwt.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserService userService;


    public String makeMeAdmin(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setRole(Role.ADMIN);
        userRepository.saveAndFlush(user);
        return user.getName() + " become Admin";
    }

    public User createUser(UserDto userDto) throws Exception {
        Optional<User> userOptional = userRepository.findUserByUsername(userDto.username());
        if (userOptional.isEmpty()) {
            User user = Optional.of(userDto).stream().map(UserDto::mapToEntity).findFirst().get();
            user.setPassword(passwordEncoder.encode(userDto.password()));
            user.setRole(Role.USER);
            user.setEnabled(false);
            return userRepository.save(user);
        } else throw new Exception("User already exists with username :: " + userDto.username());
    }


    public void deleteUser(Long id) {
        User user = userService.getUser(id);
        if (user != null) userRepository.deleteById(id);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(User::mapToDto).collect(Collectors.toList());
    }
}
