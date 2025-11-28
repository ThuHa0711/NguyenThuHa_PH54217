package com.example.nguyenthuha_ph54217.service;

import com.example.nguyenthuha_ph54217.dto.TaskRequestDto;
import com.example.nguyenthuha_ph54217.dto.TaskResponseDto;
import com.example.nguyenthuha_ph54217.entity.Task;
import com.example.nguyenthuha_ph54217.entity.User;
import com.example.nguyenthuha_ph54217.exception.AccessDeniedException;
import com.example.nguyenthuha_ph54217.exception.ResourceNotFoundException;
import com.example.nguyenthuha_ph54217.repository.TaskRepository;
import com.example.nguyenthuha_ph54217.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return user.getId();
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Kiểm tra xem User có quyền ROLE_ADMIN không
//        return authentication.getAuthorities().stream()
//                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
//        System.out.println("Authorities của User: " + authentication.getAuthorities());
//        // END DEBUG
//
//        return authentication.getAuthorities().stream()
//                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        return authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

    }

    private TaskResponseDto mapToDto(Task task) {
        TaskResponseDto dto = mapper.map(task, TaskResponseDto.class);
        if (task.getUser() != null) {
            dto.setUserId(task.getUser().getId());
            dto.setAssignedTo(task.getUser().getUsername());
        }
        return dto;
    }

    @Transactional
    public TaskResponseDto createTask(TaskRequestDto taskDto) {
        Long currentUserId = getCurrentUserId();

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại."));

        Task task = mapper.map(taskDto, Task.class);

        task.setUser(user);
        task.setCreatedAt(LocalDateTime.now());
        task.setStatus(1);

        Task savedTask = taskRepository.save(task);
        return mapToDto(savedTask);
    }

    @Override
    public Page<TaskResponseDto> getAllTasks(int pageNo, int pageSize, String sortBy, String sortDir, Integer status) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Task> tasksPage;

        if (isAdmin()) {
            if (status != null) {
                tasksPage = taskRepository.findByStatus(status, pageable);
            } else {
                tasksPage = taskRepository.findAll(pageable);
            }
        } else {
            Long currentUserId = getCurrentUserId();
            if (status != null) {
                tasksPage = taskRepository.findByUserIdAndStatus(currentUserId, status, pageable);
            } else {
                tasksPage = taskRepository.findByUserId(currentUserId, pageable);
            }
        }

        return tasksPage.map(this::mapToDto);
    }

    @Override
    public TaskResponseDto getTaskById(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId.toString()));

        if (!isAdmin() && !task.getUser().getId().equals(getCurrentUserId())) {
            throw new AccessDeniedException("Bạn không có quyền truy cập Task này.");
        }

        return mapToDto(task);
    }

    @Override
    @Transactional
    public TaskResponseDto updateTask(Long taskId, TaskRequestDto taskDto) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId.toString()));

        if (!isAdmin() && !task.getUser().getId().equals(getCurrentUserId())) {
            throw new AccessDeniedException("Bạn không có quyền sửa Task này.");
        }

        task.setTaskName(taskDto.getTaskName());
        task.setDeadline(taskDto.getDeadline());

        if (taskDto.getStatus() != null) {
            task.setStatus(taskDto.getStatus());
        }

        Task updatedTask = taskRepository.save(task);
        return mapToDto(updatedTask);
    }

    @Override
    public void deleteTask(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId.toString()));

        // Kiểm tra quyền: Chỉ ADMIN hoặc chính chủ Task mới được xóa
        if (!isAdmin() && !task.getUser().getId().equals(getCurrentUserId())) {
            throw new AccessDeniedException("Bạn không có quyền xóa Task này.");
        }

        taskRepository.delete(task);
    }
}
