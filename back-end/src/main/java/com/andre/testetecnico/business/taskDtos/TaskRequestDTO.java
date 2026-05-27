package com.andre.testetecnico.business.taskDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TaskRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 50, message = "Nome deve ter no máximo 50 caracteres")
        String name,

        @Size(max = 120, message = "Descrição deve ter no máximo 120 caracteres")
        String description,

        LocalDateTime eventAt
) {}
