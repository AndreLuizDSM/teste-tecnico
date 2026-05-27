# Análise de Incidentes

---

## Incidente 1 — Validação de entrada retornando HTTP 500

### Descrição

O endpoint `POST /user` retornava **HTTP 500 Internal Server Error** ao receber
dados inválidos — como email sem `@` ou campos obrigatórios em branco.
A resposta não continha nenhuma mensagem útil para o cliente.

---

### Causa Raiz

O `GlobalExceptionHandler` não possuía um handler específico para
`MethodArgumentNotValidException` — a exceção lançada pelo `@Valid` quando
uma validação falha. Por isso, o erro caía no handler genérico (`Exception.class`)
e retornava 500 sem contexto.

```java
// Sem handler específico, o @Valid lançava exceção não tratada
// e o handler genérico respondia com 500
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex, ...) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)...
}
```

---

### Correção Aplicada

Adicionado handler específico no `GlobalExceptionHandler`:

```java
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
```

---

### Resultado

| Cenário | Antes | Depois |
|---------|-------|--------|
| Email sem `@` | HTTP 500 sem mensagem | HTTP 400 com `"email: Email com @ obrigatório"` |
| Campo obrigatório em branco | HTTP 500 sem mensagem | HTTP 400 com campo e mensagem específicos |
| Múltiplos campos inválidos | HTTP 500 sem mensagem | HTTP 400 listando todos os erros |

---

### Medidas de Prevenção

| Medida | Descrição |
|--------|-----------|
| Handler específico para `MethodArgumentNotValidException` | Captura erros de validação antes do handler genérico |
| Annotations `@Valid` nos controllers | Ativa a validação automática dos DTOs |
| Annotations de validação nos DTOs | `@NotBlank`, `@Email`, `@Size` definem as regras |
| Logs estruturados | Facilita rastreamento de quais campos falharam |

---

## Incidente 2 — Email duplicado retornando HTTP 500

### Descrição

O endpoint `POST /user` retornava **HTTP 500 Internal Server Error** ao tentar
cadastrar um usuário com email já existente no banco de dados.

---

### Causa Raiz

Dois fatores combinados causaram o incidente:

**1. Ausência de handler específico para `DataIntegrityViolationException`**

O `GlobalExceptionHandler` não tratava violações de constraint do banco de dados —
o erro caía no handler genérico e retornava 500.

**2. Condição de corrida (Race Condition)**

Em cenários de concorrência, dois requests simultâneos com o mesmo email podiam
passar pela verificação `existsByEmail()` ao mesmo tempo e tentar persistir
simultaneamente, violando a constraint `UNIQUE` do banco.

```
Request A: existsByEmail() → false ✓
Request B: existsByEmail() → false ✓
Request A: save() → sucesso
Request B: save() → DataIntegrityViolationException 💥
```

---

### Correção Aplicada

**1. Validação prévia no `UserService`:**

```java
public UserResponseDTO saveUser(UserRequestDTO dto) {
    if (repository.existsByEmail(dto.email())) {
        throw new BadRequestException("Conta já existe: " + dto.email());
    }
    // ...
}
```

**2. Handler específico no `GlobalExceptionHandler`:**

```java
@ExceptionHandler(DataIntegrityViolationException.class)
public ResponseEntity<ErrorResponseDTO> handleDataIntegrity(
        DataIntegrityViolationException ex,
        HttpServletRequest request) {

    log.error("Data integrity erro: {} /path: {}",
              ex.getMessage(), request.getRequestURI());

    return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(buildError(
                "Recurso já existe",
                HttpStatus.CONFLICT.value(),
                request.getRequestURI(),
                "Conflict"
            ));
}
```

---

### Resultado

| Cenário | Antes | Depois |
|---------|-------|--------|
| Email duplicado (request único) | HTTP 500 sem mensagem | HTTP 400 com mensagem clara |
| Email duplicado (race condition) | HTTP 500 sem mensagem | HTTP 409 Conflict |

---

### Medidas de Prevenção

| Medida | Descrição |
|--------|-----------|
| Validação no service antes de persistir | Primeira barreira contra duplicatas |
| Constraint `UNIQUE` no banco | Segunda barreira — garante integridade mesmo em race condition |
| Handler específico para `DataIntegrityViolationException` | Resposta clara ao invés de 500 genérico |
| Logs detalhados | Facilita rastreamento do erro em produção |
