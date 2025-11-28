package com.example.nguyenthuha_ph54217.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private Integer role;
    private Integer status;
    private LocalDateTime createdAt;
}
