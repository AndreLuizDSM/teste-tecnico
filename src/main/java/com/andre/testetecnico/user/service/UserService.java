package com.andre.testetecnico.user.service;

import com.andre.testetecnico.business.userDtos.UserRequestDTO;
import com.andre.testetecnico.business.userDtos.UserResponseDTO;
import com.andre.testetecnico.user.entity.UserEntity;
import com.andre.testetecnico.user.repository.IUserRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final IUserRepositoryJpa repository;

    public UserResponseDTO saveUser(UserRequestDTO dto){
        if (repository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Conta já existe " + dto.email());
        }

        return new UserResponseDTO("exemplo", "exemplo", List.of());
    }

    public UserResponseDTO returnUser(String email){
        UserEntity entity = repository.findByEmail(email).orElseThrow(
                ()-> new IllegalArgumentException("Usuário não encontrado " + email)
        );

        return new UserResponseDTO("exemplo", "exemplo", List.of());
    }

    public void deleteUser(String email){
        if(!repository.existsByEmail(email)) {
            throw new IllegalArgumentException("Usuário não encontrado " + email);
        }

        repository.deleteByEmail(email);
    }
}
