package com.andre.testetecnico.task.repository;

import com.andre.testetecnico.task.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITaskRepositoryJpa extends JpaRepository<TaskEntity, String> {


}
