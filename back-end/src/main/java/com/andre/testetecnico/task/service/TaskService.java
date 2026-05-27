package com.andre.testetecnico.task.service;

import com.andre.testetecnico.business.exceptions.ResourceNotFoundException;
import com.andre.testetecnico.business.exceptions.UnauthorizedException;
import com.andre.testetecnico.business.mapper.TaskMapper;
import com.andre.testetecnico.business.taskDtos.TaskRequestDTO;
import com.andre.testetecnico.business.taskDtos.TaskResponseDTO;
import com.andre.testetecnico.security.JwtUtil;
import com.andre.testetecnico.task.entity.TaskEntity;
import com.andre.testetecnico.task.repository.ITaskRepositoryJpa;
import com.andre.testetecnico.user.entity.UserEntity;
import com.andre.testetecnico.user.repository.IUserRepositoryJpa;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskService {

    private final ITaskRepositoryJpa taskRepository;
    private final IUserRepositoryJpa userRepository;
    private final TaskMapper mapper;
    private final JwtUtil jwtUtil;

    public TaskResponseDTO saveTask(TaskRequestDTO dto, String token) {
        String email = getEmailFromToken(token);
        log.info("Criando tarefa para o usuário: " + email);

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Usuário não encontrado: " + email));


        TaskEntity task = mapper.requestToEntity(dto);
        task.setUser(user);
        task.setCreatedDate(LocalDateTime.now());

        log.info("Tarefa criada para o usuário: " + email);
        return mapper.entityToResponse(taskRepository.save(task));
    }

    public List<TaskResponseDTO> findTask(String token) {
        String email = getEmailFromToken(token);
        log.info("Buscando tarefas do usuário: " + email);

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Usuário não encontrado: " + email));

        return taskRepository.findByUser(user)
                .stream()
                .map(mapper::entityToResponse)
                .toList();
    }

    public void deleteTask(String id) {
        log.info("Deletando tarefa id: " + id);

        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tarefa não encontrada: " + id);
        }

        taskRepository.deleteById(id);
        log.info("Tarefa deletada com sucesso: "+ id);
    }

    //Extrair email do token
    private String getEmailFromToken(String token){
        return jwtUtil.extrairEmailToken(token.substring(7));
    }
}
