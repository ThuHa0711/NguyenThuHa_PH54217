package com.example.nguyenthuha_ph54217.controller;

import com.example.nguyenthuha_ph54217.dto.TaskRequestDto;
import com.example.nguyenthuha_ph54217.dto.TaskResponseDto;
import com.example.nguyenthuha_ph54217.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskRequestDto taskDto) {
        TaskResponseDto createdTask = taskService.createTask(taskDto);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponseDto>> getAllTasks(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "deadline", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,

            @RequestParam(value = "status", required = false) Integer status // 1: Pending, 2: Completed
    ) {
        Page<TaskResponseDto> tasks = taskService.getAllTasks(pageNo, pageSize, sortBy, sortDir, status);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable(name = "id") Long taskId) {
        TaskResponseDto task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable(name = "id") Long taskId,
            @Valid @RequestBody TaskRequestDto taskDto) {

        TaskResponseDto updatedTask = taskService.updateTask(taskId, taskDto);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable(name = "id") Long taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>("Task đã được xóa thành công!", HttpStatus.OK);
    }
}
