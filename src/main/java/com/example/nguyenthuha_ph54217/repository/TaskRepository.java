package com.example.nguyenthuha_ph54217.repository;

import com.example.nguyenthuha_ph54217.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);

    Optional<Task> findByIdAndUserId(Long taskId, Long userId);

    Page<Task> findByUserId(Long userId, Pageable pageable);

    Page<Task> findByStatus(Integer status, Pageable pageable);

    Page<Task> findByUserIdAndStatus(Long userId, Integer status, Pageable pageable);
}
