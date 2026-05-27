package com.andre.testetecnico.user.service;

import com.andre.testetecnico.business.exceptions.BadRequestException;
import com.andre.testetecnico.business.exceptions.ResourceNotFoundException;
import com.andre.testetecnico.business.exceptions.UnauthorizedException;
import com.andre.testetecnico.business.mapper.UserMapper;
import com.andre.testetecnico.business.userDtos.UserLoginDTO;
import com.andre.testetecnico.business.userDtos.UserRequestDTO;
import com.andre.testetecnico.business.userDtos.UserResponseDTO;
import com.andre.testetecnico.security.JwtUtil;
import com.andre.testetecnico.user.entity.UserEntity;
import com.andre.testetecnico.user.repository.IUserRepositoryJpa;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final IUserRepositoryJpa repository;
    private final UserMapper mapper;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserResponseDTO saveUser(UserRequestDTO dto){

        if (repository.existsByEmail(dto.email())) {
            throw new BadRequestException("Conta já existe " + dto.email());
        }

        UserEntity entity = mapper.requestToEntity(dto);
        entity.setPassword(passwordEncoder.encode(dto.password()));

        log.info("Email criado: " + dto.email());
        return mapper.entityToResponse(repository.save(entity));
    }

    public String loginUser(UserLoginDTO userDto){
        try {

            //AuthenticationManager compara os valores de userDto com o do banco de dados,usando loadByUserName
            //de baixo dos panos
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.email(), userDto.password())
            );

            // Se o userDto for incorreto , a requisição cairá no catch antes do return.
            log.info("Login feito: " + authentication.getName());
            return "Bearer " + jwtUtil.generateToken(authentication.getName());

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

    public UserResponseDTO returnUser(String email){

        log.info("Procurando email: " + email);
        UserEntity entity = repository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("Usuário não encontrado " + email)
        );

        log.info("Usuário retornado: " + entity.getName());
        return mapper.entityToResponse(entity);
    }

    public void deleteUser(String email){

        if(!repository.existsByEmail(email)) {
            throw new ResourceNotFoundException("Usuário não encontrado " + email);
        }

        log.info("Email para delete: "+ email);
        repository.deleteByEmail(email);
    }
}
