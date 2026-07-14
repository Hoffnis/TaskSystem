package com.taks.demo.service;

import com.taks.demo.dto.request.TaskRequest;
import com.taks.demo.dto.response.TaskResponse;
import com.taks.demo.entity.Task;
import com.taks.demo.enums.TaskStatus;
import com.taks.demo.exception.ResourceNotFoundException;
import com.taks.demo.mapper.TaskMapper;
import com.taks.demo.repository.TaskRepository;
import com.taks.demo.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository repository;

    @Mock
    private TaskMapper mapper;

    @InjectMocks
    private TaskServiceImpl service;

    private TaskRequest request;
    private Task task;
    private TaskResponse response;

    @BeforeEach
    void setUp() {

        request = new TaskRequest(
                "Título",
                "Descrição",
                TaskStatus.PENDENTE,
                "Hoffnis"
        );

        task = Task.builder()
                .id(1L)
                .titulo("Título")
                .descricao("Descrição")
                .status(TaskStatus.PENDENTE)
                .responsavel("Hoffnis")
                .build();

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
    void create_ShouldReturnTaskResponse() {

        when(mapper.toEntity(request)).thenReturn(task);
        when(repository.save(task)).thenReturn(task);
        when(mapper.toResponse(task)).thenReturn(response);

        TaskResponse result = service.create(request);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Título", result.titulo());
        assertEquals(TaskStatus.PENDENTE, result.status());

        verify(mapper).toEntity(request);
        verify(repository).save(task);
        verify(mapper).toResponse(task);
    }

    @Test
    @DisplayName("Deve retornar uma tarefa pelo ID")
    void findById_ShouldReturnTaskResponse() {

        when(repository.findById(1L)).thenReturn(Optional.of(task));
        when(mapper.toResponse(task)).thenReturn(response);

        TaskResponse result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Título", result.titulo());

        verify(repository).findById(1L);
        verify(mapper).toResponse(task);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID não existir")
    void findById_ShouldThrowException() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(1L)
        );

        verify(repository).findById(1L);
        verify(mapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Deve listar tarefas sem filtros")
    void searchList_ShouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Task> page =
                new PageImpl<>(List.of(task));

        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toResponse(task)).thenReturn(response);

        Page<TaskResponse> result =
                service.searchList(null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("Título", result.getContent().get(0).titulo());

        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Deve excluir tarefa existente")
    void delete_ShouldDeleteTask() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(task));

        service.delete(1L);

        verify(repository).delete(task);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir tarefa inexistente")
    void delete_ShouldThrowException() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.delete(1L)
        );

        verify(repository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve atualizar tarefa com sucesso")
    void update_ShouldUpdateTask() {

        when(repository.findById(1L)).thenReturn(Optional.of(task));
        when(repository.save(any(Task.class))).thenReturn(task);
        when(mapper.toResponse(any(Task.class))).thenReturn(response);

        TaskResponse result = service.update(1L, request);

        assertNotNull(result);

        if (request.status() == TaskStatus.CONCLUIDA) {
            assertNotNull(task.getDataConclusao());
        }

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(task);
        verify(mapper, times(1)).toResponse(task);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar tarefa inexistente")
    void update_ShouldThrowException() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, request));

        verify(repository, never()).save(any(Task.class));
        verify(mapper, never()).toResponse(any());
    }

}
