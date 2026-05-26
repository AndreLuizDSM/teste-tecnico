package com.andre.testetecnico.business.userDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
        @NotBlank @Email
        String email,
        @NotBlank
        String name,
        @NotBlank
        String password

) {
}
