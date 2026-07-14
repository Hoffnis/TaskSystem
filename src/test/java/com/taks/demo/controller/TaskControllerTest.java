package com.taks.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taks.demo.dto.request.TaskRequest;
import com.taks.demo.dto.response.TaskResponse;
import com.taks.demo.enums.TaskStatus;
import com.taks.demo.security.JwtAuthenticationFilter;
import com.taks.demo.security.JwtService;
import com.taks.demo.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        value = TaskController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        },
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        JwtAuthenticationFilter.class
                }
        )
)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private JwtService jwtService;

    private TaskRequest request;
    private TaskResponse response;
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @BeforeEach
    void setUp() {

        request = new TaskRequest(
                "Título",
                "Descrição",
                TaskStatus.PENDENTE,
                "Hoffnis"
        );

        response = new TaskResponse(
                1L,
                "Título",
                "Descrição",
                TaskStatus.PENDENTE,
                LocalDateTime.now(),
                null,
                "Hoffnis"
        );
    }

    @Test
    @DisplayName("Deve criar uma tarefa com sucesso")
    void create_ShouldReturnCreated() throws Exception {

        when(taskService.create(any(TaskRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Título"))
                .andExpect(jsonPath("$.descricao").value("Descrição"))
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.responsavel").value("Hoffnis"));

        verify(taskService).create(any(TaskRequest.class));
    }

    @Test
    @DisplayName("Deve buscar tarefa por ID")
    void findById_ShouldReturnTask() throws Exception {

        when(taskService.findById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Título"))
                .andExpect(jsonPath("$.descricao").value("Descrição"))
                .andExpect(jsonPath("$.status").value("PENDENTE"));

        verify(taskService).findById(1L);
    }

    @Test
    @DisplayName("Deve listar tarefas")
    void findAll_ShouldReturnPage() throws Exception {

        when(taskService.searchList(any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(response)));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].titulo").value("Título"))
                .andExpect(jsonPath("$.content[0].status").value("PENDENTE"));

        verify(taskService).searchList(any(), any(), any());
    }

    @Test
    @DisplayName("Deve atualizar uma tarefa")
    void update_ShouldReturnUpdatedTask() throws Exception {

        when(taskService.update(eq(1L), any(TaskRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Título"))
                .andExpect(jsonPath("$.status").value("PENDENTE"));

        verify(taskService).update(eq(1L), any(TaskRequest.class));
    }

    @Test
    @DisplayName("Deve excluir uma tarefa")
    void delete_ShouldReturnNoContent() throws Exception {

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService).delete(1L);
    }

}