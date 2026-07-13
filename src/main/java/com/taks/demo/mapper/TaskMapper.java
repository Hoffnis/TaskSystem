package com.taks.demo.mapper;

import com.taks.demo.dto.request.TaskRequest;
import com.taks.demo.dto.response.TaskResponse;
import com.taks.demo.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(TaskRequest request) {
        return Task.builder()
                .titulo(request.titulo())
                .descricao(request.descricao())
                .status(request.status())
                .responsavel(request.responsavel())
                .build();
    }

    public TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitulo(),
                task.getDescricao(),
                task.getStatus(),
                task.getDataCriacao(),
                task.getDataConclusao(),
                task.getResponsavel()
        );
    }
}