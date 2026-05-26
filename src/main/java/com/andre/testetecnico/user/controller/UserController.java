package com.andre.testetecnico.user.controller;

import com.andre.testetecnico.business.userDtos.UserRequestDTO;
import com.andre.testetecnico.business.userDtos.UserResponseDTO;
import com.andre.testetecnico.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService service;


    @PostMapping
    public ResponseEntity<UserResponseDTO> salvarUsuario(@Valid @RequestBody UserRequestDTO usuarioDTO) {
        return ResponseEntity.ok(service.saveUser(usuarioDTO));
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> buscarEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(service.returnUser(email));
    }

    @DeleteMapping("/{email}") // var na URI , nome deve ser igual ao parâmetro
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email) {
        service.deleteUser(email);
        return ResponseEntity.ok().build();
    }
}
