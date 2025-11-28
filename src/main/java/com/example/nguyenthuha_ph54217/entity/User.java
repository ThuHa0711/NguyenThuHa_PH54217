package com.example.nguyenthuha_ph54217.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 100, nullable = false, unique = true)
    private String username;

    @Column(name = "full_name", length = 150, nullable = false)
    private String fullName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", length = 150, nullable = false, unique = true)
    private String email;



    // role TINYINT: 1 = USER, 2 = ADMIN.
//    @Enumerated(EnumType.ORDINAL) // Lưu trữ dưới dạng số (ordinal)
//    @Column(name = "role", nullable = false)
//    private Role role;
    @Column(name = "role", nullable = false)
    private Integer role;

    // status TINYINT: 1 = active, 0 = inactive
    @Column(name = "status", nullable = false)
    private Integer status; // Hoặc Byte, thể hiện trạng thái hoạt động

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;
}
