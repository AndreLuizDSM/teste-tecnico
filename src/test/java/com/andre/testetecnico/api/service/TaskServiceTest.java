package com.andre.testetecnico.api.service;

import com.andre.testetecnico.business.exceptions.ResourceNotFoundException;
import com.andre.testetecnico.business.exceptions.UnauthorizedException;
import com.andre.testetecnico.business.mapper.TaskMapper;
import com.andre.testetecnico.business.taskDtos.TaskRequestDTO;
import com.andre.testetecnico.business.taskDtos.TaskResponseDTO;
import com.andre.testetecnico.security.JwtUtil;
import com.andre.testetecnico.task.entity.TaskEntity;
import com.andre.testetecnico.task.repository.ITaskRepositoryJpa;
import com.andre.testetecnico.task.service.TaskService;
import com.andre.testetecnico.user.entity.UserEntity;
import com.andre.testetecnico.user.repository.IUserRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private ITaskRepositoryJpa taskRepository;
    @Mock
    private IUserRepositoryJpa userRepository;
    @Mock
    private TaskMapper mapper;
    @Mock
    private JwtUtil jwtUtil;

    private TaskRequestDTO taskRequestDTO;
    private TaskResponseDTO taskResponseDTO;
    private TaskEntity taskEntity;
    private TaskEntity savedEntity;
    private UserEntity userEntity;

    private String token;
    private String email;
    private String taskId;

    @BeforeEach
    void setup() {
        email = "andre@email.com";
        token = "Bearer tokenJWT_exemplo";
        taskId = "1";

        taskRequestDTO = new TaskRequestDTO("Tarefa 1", "Descrição da tarefa", LocalDateTime.now().plusDays(1));
        taskResponseDTO = new TaskResponseDTO(1L, "Tarefa 1", "Descrição da tarefa", LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        userEntity = new UserEntity();
        taskEntity = new TaskEntity();
        savedEntity = new TaskEntity();
    }

                        // saveTask

    @Test
    void saveTaskSucessed() {
        when(jwtUtil.extrairEmailToken(token.substring(7))).thenReturn(email);
        // Retorna Optional , pois o banco de dados possui esse retorno
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(mapper.requestToEntity(taskRequestDTO)).thenReturn(taskEntity);
        when(taskRepository.save(taskEntity)).thenReturn(savedEntity);
        when(mapper.entityToResponse(savedEntity)).thenReturn(taskResponseDTO);

        TaskResponseDTO result = taskService.saveTask(taskRequestDTO, token);

        assertNotNull(result);
        // Nome do DTO
        assertEquals("Tarefa 1", result.name());

        verify(taskRepository).save(taskEntity);
        verify(mapper).entityToResponse(savedEntity);
        verifyNoMoreInteractions(taskRepository, mapper);
    }

    @Test
    void saveTask_throwUnauthorizedException() {
        // Substring para retirar o "Bearer "
        when(jwtUtil.extrairEmailToken(token.substring(7))).thenReturn(email);
        when(userRepository.findByEmail(email)).thenThrow(
                new UnauthorizedException("Usuário não encontrado: " + email));

        UnauthorizedException e = assertThrows(UnauthorizedException.class,
                () -> taskService.saveTask(taskRequestDTO, token));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Usuário não encontrado: " + email));

        verifyNoMoreInteractions(userRepository, taskRepository);
    }

                    // findTask

    @Test
    void findTaskSucessed() {
        when(jwtUtil.extrairEmailToken(token.substring(7))).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(taskRepository.findByUser(userEntity)).thenReturn(List.of(taskEntity));
        when(mapper.entityToResponse(taskEntity)).thenReturn(taskResponseDTO);

        // Finda tasks pelo token que já está instanciado e foi tirado o "Bearer "
        List<TaskResponseDTO> result = taskService.findTask(token);

        assertNotNull(result);
        assertThat(result.get(0).name(), is("Tarefa 1"));

        verifyNoMoreInteractions(taskRepository, mapper);
    }

    @Test
    void findTask_throwUnauthorizedException() {
        when(jwtUtil.extrairEmailToken(token.substring(7))).thenReturn(email);
        when(userRepository.findByEmail(email)).thenThrow(
                new UnauthorizedException("Usuário não encontrado: " + email));

        UnauthorizedException e = assertThrows(UnauthorizedException.class,
                () -> taskService.findTask(token));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Usuário não encontrado: " + email));

        verifyNoMoreInteractions(userRepository, taskRepository);
    }

                         // deleteTask

    @Test
    void deleteTaskSucessed() {
        when(taskRepository.existsById(taskId)).thenReturn(true);

        taskService.deleteTask(taskId);

        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void deleteTask_throwResourceNotFoundException() {
        when(taskRepository.existsById(taskId)).thenThrow(
                new ResourceNotFoundException("Tarefa não encontrada: " + taskId));

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> taskService.deleteTask(taskId));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Tarefa não encontrada: " + taskId));

        verifyNoMoreInteractions(taskRepository);
    }

                        // updateTask

    @Test
    void updateTaskSucessed() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));
        when(taskRepository.save(taskEntity)).thenReturn(savedEntity);
        when(mapper.entityToResponse(savedEntity)).thenReturn(taskResponseDTO);

        TaskResponseDTO result = taskService.updateTask(taskRequestDTO, taskId);

        assertNotNull(result);
        assertEquals("Tarefa 1", result.name());


        verify(taskRepository).save(taskEntity);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void updateTask_naoEncontrado_lancaResourceNotFoundException() {
        when(taskRepository.findById(taskId)).thenThrow(
                new ResourceNotFoundException("Tarefa não encontrada: " + taskId));

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> taskService.updateTask(taskRequestDTO, taskId));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Tarefa não encontrada: " + taskId));

        verifyNoMoreInteractions(taskRepository, mapper);
    }
}