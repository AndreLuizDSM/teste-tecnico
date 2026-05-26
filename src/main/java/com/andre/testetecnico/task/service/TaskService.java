package com.andre.testetecnico.task.service;

import com.andre.testetecnico.business.taskDtos.TaskRequestDTO;
import com.andre.testetecnico.business.taskDtos.TaskResponseDTO;
import com.andre.testetecnico.task.repository.ITaskRepositoryJpa;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskService {

    private final ITaskRepositoryJpa repository;

    public TaskResponseDTO saveTask (TaskRequestDTO dto, String token){
        return new TaskResponseDTO();
    }

    public List<TaskResponseDTO> findTask (String token){
        return new ArrayList<TaskResponseDTO>();
    }

    public void deleteTask (String token){

    }

    public TaskResponseDTO updateTask (TaskRequestDTO dto, String token){
        return new TaskResponseDTO();
    }

}
