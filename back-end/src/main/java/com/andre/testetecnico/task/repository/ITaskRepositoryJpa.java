package com.andre.testetecnico.task.repository;

import com.andre.testetecnico.business.taskDtos.TaskResponseDTO;
import com.andre.testetecnico.task.entity.TaskEntity;
import com.andre.testetecnico.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITaskRepositoryJpa extends JpaRepository<TaskEntity, String> {

    List<TaskEntity> findByUser (UserEntity user);
}
