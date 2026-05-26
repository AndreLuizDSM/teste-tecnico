package com.andre.testetecnico.user.service;

import com.andre.testetecnico.user.repository.IUserRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final IUserRepositoryJpa repository;


}
