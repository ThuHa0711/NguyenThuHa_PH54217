package com.example.nguyenthuha_ph54217.service;

import com.example.nguyenthuha_ph54217.dto.UserRequestDto;
import com.example.nguyenthuha_ph54217.dto.UserResponseDto;
import com.example.nguyenthuha_ph54217.entity.User;
import com.example.nguyenthuha_ph54217.exception.ResourceNotFoundException;
import com.example.nguyenthuha_ph54217.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder; // <-- IMPORT VÀ INJECT

    // 1. Xem danh sách (Đã có)
    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> mapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    // 2. Xem chi tiết (MỚI)
    @Override
    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));
        return mapper.map(user, UserResponseDto.class);
    }

    // 3. Thêm (MỚI)
    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        // Kiểm tra username/email đã tồn tại
        if(userRepository.existsByUsername(userRequestDto.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }

        User newUser = mapper.map(userRequestDto, User.class);
        // Mã hóa mật khẩu trước khi lưu
        newUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        User savedUser = userRepository.save(newUser);
        return mapper.map(savedUser, UserResponseDto.class);
    }

    // 4. Sửa (MỚI)
    @Override
    public UserResponseDto updateUser(Long userId, UserRequestDto userRequestDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        // Cập nhật các trường từ DTO
        existingUser.setUsername(userRequestDto.getUsername());
        existingUser.setFullName(userRequestDto.getFullName());
        existingUser.setEmail(userRequestDto.getEmail());
        existingUser.setRole(userRequestDto.getRole());
        existingUser.setStatus(userRequestDto.getStatus());

        // Nếu có mật khẩu mới được truyền vào, thì mã hóa và cập nhật
        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        return mapper.map(updatedUser, UserResponseDto.class);
    }

    // 5. Xóa (MỚI)
    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));
        // Đảm bảo không xóa tài khoản Admin đang dùng (tùy chọn)
        // if (user.getRole() == 2) {
        //     throw new RuntimeException("Không được xóa tài khoản Admin!");
        // }
        userRepository.delete(user);
    }
}
