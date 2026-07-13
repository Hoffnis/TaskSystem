package com.taks.demo.service.impl;

import com.taks.demo.dto.request.TaskRequest;
import com.taks.demo.dto.response.TaskResponse;
import com.taks.demo.entity.Task;
import com.taks.demo.enums.TaskStatus;
import com.taks.demo.exception.ResourceNotFoundException;
import com.taks.demo.mapper.TaskMapper;
import com.taks.demo.repository.TaskRepository;
import com.taks.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;

    @Override
    public TaskResponse create(TaskRequest request) {

        Task task = mapper.toEntity(request);
        Task savedTask = repository.save(task);
        return mapper.toResponse(savedTask);
    }

    @Override
    public TaskResponse findById(Long id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada"));

        return mapper.toResponse(task);
    }

    @Override
    public Page<TaskResponse> searchList(TaskStatus status,
                                     String responsavel,
                                     Pageable pageable) {

        Page<Task> tasks;

        if (status != null && responsavel != null && !responsavel.isBlank()) {

            tasks = repository.findByStatusAndResponsavelContainingIgnoreCase(
                    status,
                    responsavel,
                    pageable
            );

        } else if (status != null) {

            tasks = repository.findByStatus(status, pageable);

        } else if (responsavel != null && !responsavel.isBlank()) {

            tasks = repository.findByResponsavelContainingIgnoreCase(
                    responsavel,
                    pageable
            );

        } else {

            tasks = repository.findAll(pageable);

        }

        return tasks.map(mapper::toResponse);

    }

    @Override
    public TaskResponse update(Long id, TaskRequest request) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com id: " + id));

        task.setTitulo(request.titulo());
        task.setDescricao(request.descricao());
        task.setStatus(request.status());
        task.setResponsavel(request.responsavel());

        if (task.getStatus() == TaskStatus.CONCLUIDA) {
            task.setDataConclusao(LocalDateTime.now());
        } else {
            task.setDataConclusao(null);
        }

        Task updatedTask = repository.save(task);

        return mapper.toResponse(updatedTask);
    }

    @Override
    public void delete(Long id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com id: " + id));

        repository.delete(task);
    }
}
