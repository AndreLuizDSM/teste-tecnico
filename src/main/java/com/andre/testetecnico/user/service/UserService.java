package com.andre.testetecnico.user.service;

import com.andre.testetecnico.business.userDtos.UserRequestDTO;
import com.andre.testetecnico.business.userDtos.UserResponseDTO;
import com.andre.testetecnico.user.repository.IUserRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final IUserRepositoryJpa repository;

    public UserResponseDTO saveUser(UserRequestDTO dto){
        return new UserResponseDTO();
    }

    public UserResponseDTO returnUser(String email){
        return new UserResponseDTO();
    }

    public void deleteUser(String email){

    }
}
