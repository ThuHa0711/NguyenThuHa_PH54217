package com.example.nguyenthuha_ph54217.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequestDto {
    @NotBlank(message = "Tên công việc không được để trống")
    private String taskName;

    @NotNull(message = "Deadline không được để trống")
    @FutureOrPresent(message = "Deadline phải là thời điểm hiện tại hoặc tương lai")
    private LocalDateTime deadline;

    private Integer status; // 1: Pending, 2: Completed
}
