package com.example.nguyenthuha_ph54217.service;

import com.example.nguyenthuha_ph54217.dto.UserRequestDto;
import com.example.nguyenthuha_ph54217.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    // 1. Xem danh sách
    List<UserResponseDto> getAllUsers();

    // 2. Xem chi tiết
    UserResponseDto getUserById(Long userId);

    // 3. Thêm
    UserResponseDto createUser(UserRequestDto userRequestDto);

    // 4. Sửa
    UserResponseDto updateUser(Long userId, UserRequestDto userRequestDto);

    // 5. Xóa
    void deleteUser(Long userId);
}
