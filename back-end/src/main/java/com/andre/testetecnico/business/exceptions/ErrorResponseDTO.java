package com.andre.testetecnico.business.exceptions;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponseDTO (
        LocalDateTime timeStamp,
        int status,
        String message,
        String path,
        String erro
){
}
