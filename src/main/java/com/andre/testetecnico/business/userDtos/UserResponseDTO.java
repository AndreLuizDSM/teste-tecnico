package com.andre.testetecnico.business.userDtos;

import com.andre.testetecnico.business.taskDtos.TaskResponseDTO;
import com.andre.testetecnico.task.entity.TaskEntity;
import jakarta.annotation.Nullable;

import java.util.List;

public record UserResponseDTO (

        String email,
        String nome,
        @Nullable
        List<TaskResponseDTO> tasks
){
}
