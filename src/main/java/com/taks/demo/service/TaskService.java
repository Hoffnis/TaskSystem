package com.taks.demo.service;

import com.taks.demo.dto.request.TaskRequest;
import com.taks.demo.dto.response.TaskResponse;
import com.taks.demo.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    TaskResponse create(TaskRequest request);

    TaskResponse findById(Long id);

    Page<TaskResponse> searchList(TaskStatus status, String responsavel, Pageable pageable);

    TaskResponse update(Long id, TaskRequest request);

    void delete(Long id);
}
