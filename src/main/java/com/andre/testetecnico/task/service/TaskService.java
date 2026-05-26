package com.andre.testetecnico.task.service;

import com.andre.testetecnico.task.repository.ITaskRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final ITaskRepositoryJpa repository;


}
