package com.taks.demo.dto.response;

import com.taks.demo.enums.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponse(

        Long id,

        String titulo,

        String descricao,

        TaskStatus status,

        LocalDateTime dataCriacao,

        LocalDateTime dataConclusao,

        String responsavel

) {
}
