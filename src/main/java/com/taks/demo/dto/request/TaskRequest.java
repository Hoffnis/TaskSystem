package com.taks.demo.dto.request;

import com.taks.demo.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskRequest(

        @NotBlank
        @Size(max = 200)
        String titulo,

        @Size(max = 1000)
        String descricao,

        @NotNull
        TaskStatus status,

        @NotBlank
        @Size(max = 100)
        String responsavel

) {
}
