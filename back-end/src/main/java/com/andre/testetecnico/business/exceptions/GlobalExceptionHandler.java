package com.andre.testetecnico.business.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Exception caso eu não encontre task/user
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                   HttpServletRequest request) {
        log.error("Not found erro: " + ex.getMessage() + " /path: " + request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildError(ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI(),
                "Not Found"));
    }

    // Exception caso eu não consiga realizar login, ou não tenha permissão.
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedException(UnauthorizedException ex,
                                                                        HttpServletRequest request) {
        log.error("Unauthorized erro: " + ex.getMessage() + " /path: " + request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildError(ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                request.getRequestURI(),
                "Unauthorized"));
    }

    // Exception caso eu faço um register com email já existente
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestException(BadRequestException ex,
                                                                           HttpServletRequest request) {
        log.error("Bad request erro: " + ex.getMessage() + " /path: " + request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildError(ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI(),
                "Bad Request"));
    }

    // Exception caso usuário tente passar credênciais inválidas com o que foi definido no DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.error("Validation erro: {} /path: {}", message, request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(
                        message,
                        HttpStatus.BAD_REQUEST.value(),
                        request.getRequestURI(),
                        "Bad Request"
                ));
    }

    // Exception caso BadRequest falhe , e chegue 2 requisições para registrar com email iguais.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrity(DataIntegrityViolationException ex,
                                                                HttpServletRequest request) {
        log.error("Data integrity erro: " + ex.getMessage() + " /path: " + request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError("Recurso já existe", HttpStatus.CONFLICT.value(), request.getRequestURI(), "Conflict"));
    }

    // Exception 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex,
                                                          HttpServletRequest request) {
        log.error("Generic erro /path: " + request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError("Erro interno do servidor", 500, request.getRequestURI(), "Internal Server Error"));
    }

    // DTO para um retorno de Erro completo.
    private ErrorResponseDTO buildError (String message, int status, String path, String erro) {
        // Criar um erroDTO para passar detalhadamente o erro com o Token JWT
        return  ErrorResponseDTO.builder()
                .timeStamp(LocalDateTime.now())
                .status(status)
                .message(message)
                .path(path)
                .erro(erro)
                .build();
    }
}
