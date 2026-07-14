package com.taks.demo.controller;

import com.taks.demo.dto.request.TaskRequest;
import com.taks.demo.dto.response.TaskResponse;
import com.taks.demo.enums.TaskStatus;
import com.taks.demo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Criar nova tarefa")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@Valid @RequestBody TaskRequest request) {
        return taskService.create(request);
    }

    @Operation(summary = "Atualizar tarefa pelo id")
    @PutMapping("/{id}")
    public TaskResponse update(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {

        return taskService.update(id, request);
    }

    @Operation(summary = "Buscar tarefa pelo id")
    @GetMapping("/{id}")
    public TaskResponse findById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @Operation(summary = "Buscar lista de tarefas, com filtros opcionais de Status e Responsavel")
    @GetMapping
    public Page<TaskResponse> findAll(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) String responsavel,
            Pageable pageable) {

        return taskService.searchList(status, responsavel, pageable);
    }

    @Operation(summary = "Apagar tarefa pelo id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

}