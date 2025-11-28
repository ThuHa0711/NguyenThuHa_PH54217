package com.example.nguyenthuha_ph54217.controller;

import com.example.nguyenthuha_ph54217.dto.UserRequestDto;
import com.example.nguyenthuha_ph54217.dto.UserResponseDto;
import com.example.nguyenthuha_ph54217.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users") // Chỉ ADMIN mới truy cập được /api/admin/** (Đã cấu hình trong Security)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 1. Xem danh sách tất cả người dùng (GET /api/admin/users)
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 2. Xem chi tiết người dùng (GET /api/admin/users/{id})
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long userId) {
        UserResponseDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // 3. Thêm người dùng mới (POST /api/admin/users)
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto newUser = userService.createUser(userRequestDto);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // 4. Sửa thông tin người dùng (PUT /api/admin/users/{id})
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable("id") Long userId,
            @Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto updatedUser = userService.updateUser(userId, userRequestDto);
        return ResponseEntity.ok(updatedUser);
    }

    // 5. Xóa người dùng (DELETE /api/admin/users/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("Người dùng có ID " + userId + " đã được xóa thành công.");
    }
}
