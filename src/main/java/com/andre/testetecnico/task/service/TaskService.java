package com.andre.testetecnico.task.service;

import com.andre.testetecnico.business.taskDtos.TaskRequestDTO;
import com.andre.testetecnico.business.taskDtos.TaskResponseDTO;
import com.andre.testetecnico.task.repository.ITaskRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
