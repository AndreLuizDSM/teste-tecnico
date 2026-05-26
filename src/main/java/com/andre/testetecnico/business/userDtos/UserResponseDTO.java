package com.andre.testetecnico.business.userDtos;

import com.andre.testetecnico.business.taskDtos.TaskResponseDTO;
import com.andre.testetecnico.task.entity.TaskEntity;
import jakarta.annotation.Nullable;

import java.util.List;

public record UserResponseDTO (
        String id,
        String email,
        String name,
        @Nullable
        List<TaskResponseDTO> tasks
){
}
