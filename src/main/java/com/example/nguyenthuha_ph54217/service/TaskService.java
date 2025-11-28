package com.example.nguyenthuha_ph54217.service;

import com.example.nguyenthuha_ph54217.dto.TaskRequestDto;
import com.example.nguyenthuha_ph54217.dto.TaskResponseDto;
import org.springframework.data.domain.Page;


public interface TaskService {
    TaskResponseDto createTask(TaskRequestDto taskDto);

    Page<TaskResponseDto> getAllTasks(int pageNo, int pageSize, String sortBy, String sortDir, Integer status);

    TaskResponseDto getTaskById(Long taskId);

    TaskResponseDto updateTask(Long taskId, TaskRequestDto taskDto);

    void deleteTask(Long taskId);
}
