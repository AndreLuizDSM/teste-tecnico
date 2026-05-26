package com.andre.testetecnico.user.repository;

import com.andre.testetecnico.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepositoryJpa extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail (String email);

    boolean existsByEmail(String email);

    @Transactional
    void deleteByEmail (String email);
}
