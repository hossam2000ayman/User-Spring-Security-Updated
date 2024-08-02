package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.dto.UserDto;
import com.example.springsecurityjwt.entity.User;
import com.example.springsecurityjwt.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;


    @PutMapping("{id}")
    public ResponseEntity<String> makeMeAdmin(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(adminService.makeMeAdmin(id));
    }


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createUser(userDto));
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getAllUsers());
    }
}
