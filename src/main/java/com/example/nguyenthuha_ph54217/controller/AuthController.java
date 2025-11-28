package com.example.nguyenthuha_ph54217.controller;

import com.example.nguyenthuha_ph54217.dto.JwtAuthResponseDto;
import com.example.nguyenthuha_ph54217.dto.LoginRequestDto;
import com.example.nguyenthuha_ph54217.dto.RegisterRequestDto;
import com.example.nguyenthuha_ph54217.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDto registerDto) {
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginDto) {
        JwtAuthResponseDto response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }
}
