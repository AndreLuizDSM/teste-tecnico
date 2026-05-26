package com.andre.testetecnico.user.service;

import com.andre.testetecnico.business.mapper.UserMapper;
import com.andre.testetecnico.business.userDtos.UserRequestDTO;
import com.andre.testetecnico.business.userDtos.UserResponseDTO;
import com.andre.testetecnico.user.entity.UserEntity;
import com.andre.testetecnico.user.repository.IUserRepositoryJpa;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final IUserRepositoryJpa repository;
    private final UserMapper mapper;

    public UserResponseDTO saveUser(UserRequestDTO dto){
        if (repository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Conta já existe " + dto.email());
        }
        log.info("Email criado: " + dto.email());
        return mapper.entityToResponse(repository.save(mapper.requestToEntity(dto)));
    }

    public UserResponseDTO returnUser(String email){
        log.info("Procurando email: " + email);
        UserEntity entity = repository.findByEmail(email).orElseThrow(
                ()-> new IllegalArgumentException("Usuário não encontrado " + email)
        );
        log.info("Usuário retornado: " + entity);
        return mapper.entityToResponse(entity);
    }

    public void deleteUser(String email){
        if(!repository.existsByEmail(email)) {
            throw new IllegalArgumentException("Usuário não encontrado " + email);
        }
        log.info("Email para delete: "+ email);
        repository.deleteByEmail(email);
    }
}
