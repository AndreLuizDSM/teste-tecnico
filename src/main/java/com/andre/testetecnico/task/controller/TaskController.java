package com.andre.testetecnico.task.controller;

import com.andre.testetecnico.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/task")
@RestController
public class TaskController {

    private final TaskService service;

}
