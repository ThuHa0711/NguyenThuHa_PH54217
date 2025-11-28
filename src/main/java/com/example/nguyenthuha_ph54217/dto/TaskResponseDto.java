package com.example.nguyenthuha_ph54217.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskResponseDto {
    private Long id;
    private String taskName;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private Integer status;
    private Long userId;
    private String assignedTo;
}
