package com.andre.testetecnico.business.taskDtos;

import java.time.LocalDateTime;

public record TaskResponseDTO(

        Long id,
        String name,
        String description,
        LocalDateTime createdDate,
        LocalDateTime eventAt
) {}
