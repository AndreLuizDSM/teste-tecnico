package com.andre.testetecnico.business.mapper;

import com.andre.testetecnico.business.taskDtos.TaskRequestDTO;
import com.andre.testetecnico.business.taskDtos.TaskResponseDTO;
import com.andre.testetecnico.task.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskEntity requestToEntity(TaskRequestDTO dto);

    TaskResponseDTO entityToResponse(TaskEntity entity);

    void updateEntity(TaskRequestDTO dto, @MappingTarget TaskEntity entity);
}