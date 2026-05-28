package com.andre.testetecnico.api.service;

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
import com.andre.testetecnico.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private IUserRepositoryJpa repository;
    @Mock
    private UserMapper mapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;


    private UserRequestDTO userRequestDTO;
    private UserLoginDTO userLoginDTO;
    private UserResponseDTO userResponseDTO;
    private UserEntity userEntity;
    private UserEntity savedEntity;

    @BeforeEach
    void setup() {
        userRequestDTO = new UserRequestDTO("andre@email.com", "André", "senha123");
        userLoginDTO = new UserLoginDTO("andre@email.com", "senha123");
        userResponseDTO = new UserResponseDTO("123", "andre@email.com", "André", List.of());
        userEntity = new UserEntity();
        savedEntity = new UserEntity();
    }

                        //  saveUser

    @Test
    void saveUser_success() {
        when(repository.existsByEmail(userRequestDTO.email())).thenReturn(false);
        when(mapper.requestToEntity(userRequestDTO)).thenReturn(userEntity);
        when(passwordEncoder.encode(userRequestDTO.password())).thenReturn("senha_encoded_para_teste");
        when(repository.save(userEntity)).thenReturn(savedEntity);
        when(mapper.entityToResponse(savedEntity)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.saveUser(userRequestDTO);

        assertNotNull(result);
        assertEquals("andre@email.com", result.email());

        verify(repository).save(userEntity);
        verify(passwordEncoder).encode(userRequestDTO.password());
        verifyNoMoreInteractions(repository, mapper, passwordEncoder);
    }

    @Test
    void saveUser_emailDuplicado_lancaBadRequestException() {
        when(repository.existsByEmail(userRequestDTO.email())).thenReturn(true);

        BadRequestException e = assertThrows(BadRequestException.class,
                () -> userService.saveUser(userRequestDTO));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Conta ja existe " + userRequestDTO.email()));

        verifyNoMoreInteractions(repository, mapper);
    }

                // loginUser
    @Test
    void loginUser_success() {

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userLoginDTO.email());
        when(jwtUtil.generateToken(userLoginDTO.email())).thenReturn("tokenJWT");

        String resultToken = userService.loginUser(userLoginDTO);

        //O método generateToken , coloca um "Bearer " antes do tokenJWT
        assertEquals("Bearer tokenJWT", resultToken);
        verifyNoMoreInteractions(jwtUtil);
    }

    @Test
    void loginUser_credenciaisInvalidas_lancaUnauthorizedException() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        UnauthorizedException e = assertThrows(UnauthorizedException.class,
                () -> userService.loginUser(userLoginDTO));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Credenciais inválidas"));

        verifyNoMoreInteractions(jwtUtil);
    }

                    // returnUser
    @Test
    void returnUserSuccess() {
        when(repository.findByEmail(userRequestDTO.email())).thenReturn(Optional.of(userEntity));
        when(mapper.entityToResponse(userEntity)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.returnUser(userRequestDTO.email());

        assertNotNull(result);
        assertEquals("andre@email.com", result.email());

        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void returnUser_naoEncontrado_lancaResourceNotFoundException() {
        when(repository.findByEmail(userRequestDTO.email())).thenThrow(new ResourceNotFoundException("Usuario " +
                "nao encontrado " + userRequestDTO.email()));

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> userService.returnUser(userRequestDTO.email()));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Usuario nao encontrado " + userRequestDTO.email()));

        verifyNoMoreInteractions(repository);
    }

                    // deleteUser

    @Test
    void deleteUserSuccess() {
        when(repository.existsByEmail(userRequestDTO.email())).thenReturn(true);

        userService.deleteUser(userRequestDTO.email());

        verify(repository).deleteByEmail(userRequestDTO.email());
    }

    @Test
    void deleteUser_throwResourceNotFoundException() {
        when(repository.existsByEmail(userRequestDTO.email())).thenThrow(
               new ResourceNotFoundException("Usuario nao encontrado " + userRequestDTO.email()));

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(userRequestDTO.email()));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Usuario nao encontrado " + userRequestDTO.email()));

        verifyNoMoreInteractions(repository);
    }
}