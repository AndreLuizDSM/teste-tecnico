package com.andre.testetecnico.task.controller;

import com.andre.testetecnico.business.taskDtos.TaskRequestDTO;
import com.andre.testetecnico.business.taskDtos.TaskResponseDTO;
import com.andre.testetecnico.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/task")
@RestController
public class TaskController {

    private final TaskService service;


    @PostMapping
    public ResponseEntity<TaskResponseDTO> salvarTarefa (@Valid @RequestBody TaskRequestDTO tarefaDTO,
                                                         @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(service.saveTask(tarefaDTO, token));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> buscaListaDeTarefaPorEmail(@RequestHeader("Authorization")String token){

        //List<TarefaDTO> listaDto = tarefaService.buscarTarefaGravadaPorEmail(token); Jeito simples
        return ResponseEntity.ok(service.findTask(token));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletaTarefaPorId(@RequestParam String id) {
        service.deleteTask  (id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<TaskResponseDTO> alteraTarefa (@RequestBody TaskRequestDTO tarefaDTO,
                                                   @RequestParam("id") String id) {
        return ResponseEntity.ok(service.updateTask(tarefaDTO, id));
    }
}
