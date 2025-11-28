package com.example.nguyenthuha_ph54217.service;

import com.example.nguyenthuha_ph54217.dto.JwtAuthResponseDto;
import com.example.nguyenthuha_ph54217.dto.LoginRequestDto;
import com.example.nguyenthuha_ph54217.dto.RegisterRequestDto;

public interface AuthService {
    String register(RegisterRequestDto registerDto);

    JwtAuthResponseDto login(LoginRequestDto loginDto);
}
