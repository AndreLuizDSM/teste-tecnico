package com.andre.testetecnico.business.userDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(
        @NotBlank(message = "Email obrigatório")
        @Email(message = "Email com @ obrigatório")
        String email,
        @NotBlank(message = "Senha obrigatória")
        String password
) {
}
