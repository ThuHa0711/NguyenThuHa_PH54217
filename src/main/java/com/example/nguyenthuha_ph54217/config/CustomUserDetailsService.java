package com.example.nguyenthuha_ph54217.config;

import com.example.nguyenthuha_ph54217.entity.User;
import com.example.nguyenthuha_ph54217.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

//        String roleName;
//        if (user.getRole() == 2) {
//            roleName = "ROLE_ADMIN";
//        } else { // 1 = USER
//            roleName = "ROLE_USER";
//        }
//
//        List<GrantedAuthority> authorities = Collections.singletonList(
//                new SimpleGrantedAuthority(roleName)
//        );
        List<GrantedAuthority> authorities;

        if (user.getRole() == 2) {
            // ADMIN cần cả hai quyền để truy cập các tài nguyên yêu cầu ROLE_USER (nếu có)
            authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        } else { // 1 = USER
            authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
