package com.example.nguyenthuha_ph54217.service;

import com.example.nguyenthuha_ph54217.dto.JwtAuthResponseDto;
import com.example.nguyenthuha_ph54217.dto.LoginRequestDto;
import com.example.nguyenthuha_ph54217.dto.RegisterRequestDto;
import com.example.nguyenthuha_ph54217.entity.User;
import com.example.nguyenthuha_ph54217.exception.ResourceConflictException;
import com.example.nguyenthuha_ph54217.repository.UserRepository;
import com.example.nguyenthuha_ph54217.config.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public String register(RegisterRequestDto registerDto) {

        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new ResourceConflictException("Username đã được sử dụng!");
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new ResourceConflictException("Email đã được sử dụng!");
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setFullName(registerDto.getFullName());
        user.setEmail(registerDto.getEmail());

        user.setRole(1); // 1 = USER
        user.setStatus(1); // 1 = Active

        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        userRepository.save(user);

        return "Đăng ký thành công!";
    }

    @Override
    public JwtAuthResponseDto login(LoginRequestDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        String roleName = authentication.getAuthorities().stream()
                .findFirst().map(auth -> auth.getAuthority()).orElse("USER");

        return new JwtAuthResponseDto(token, "Bearer", loginDto.getUsername(), roleName);
    }
}
