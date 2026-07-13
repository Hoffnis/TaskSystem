package com.taks.demo.repository;

import com.taks.demo.entity.Task;
import com.taks.demo.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    Page<Task> findByResponsavelContainingIgnoreCase(String responsavel, Pageable pageable);

    Page<Task> findByStatusAndResponsavelContainingIgnoreCase(
            TaskStatus status,
            String responsavel,
            Pageable pageable
    );
}

