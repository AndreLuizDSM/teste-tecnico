package com.andre.testetecnico.business.userDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve conter @")
        String email,
        @NotBlank(message = "Nome é obrigatório")
        String name,
        @NotBlank(message = "Senha é obrigatório")
        String password

) {
}
