# Teste Técnico — Task Manager

API REST + Frontend Angular para gerenciamento de tarefas com autenticação JWT.

---

## Estrutura do Repositório

```
testetecnico/
├── back-end/          # API Spring Boot
├── front-end/         # Aplicação Angular
├── .github/workflows/ # CI/CD com GitHub Actions
└── README.md
```

---

## Tecnologias

**Backend**
- Java 21
- Spring Boot 3.5
- Spring Security + JWT
- Spring Data JPA + PostgreSQL
- MapStruct
- Lombok
- Gradle

**Frontend**
- Angular 21
- Angular Material
- TypeScript
- Reactive Forms

---

## Pré-requisitos

- Java 21+
- Node.js 22+
- PostgreSQL 15+
- Angular CLI (`npm install -g @angular/cli`)

---

## Configuração e Execução

### 1. Banco de Dados

Crie o banco de dados no PostgreSQL:

```sql
CREATE DATABASE teste_tecnico;
```

### 2. Variáveis de Ambiente

Crie um arquivo `.env` na raiz baseado no `.env.example`:

```
DB_URL=jdbc:postgresql://localhost:5432/teste_tecnico
DB_USERNAME=postgres
DB_PASSWORD=sua_senha
JWT_SECRET=seu_secret_base64
```

> O `JWT_SECRET` deve ser uma string codificada em Base64.
> Gere com: `echo -n "sua-senha-longa" | base64`

### 3. Backend

```bash
cd back-end
./gradlew bootRun
```

A API estará disponível em `http://localhost:8080`

### 4. Frontend

```bash
cd front-end
npm install
ng serve
```

A aplicação estará disponível em `http://localhost:4200`

---

## Endpoints da API

### Autenticação

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/user` | Cadastrar usuário | Não |
| POST | `/user/login` | Login — retorna Bearer token | Não |

**Exemplo — Cadastro:**
```json
POST /user
{
  "email": "user@email.com",
  "name": "André",
  "password": "senha123"
}
```

**Exemplo — Login:**
```json
POST /user/login
{
  "email": "user@email.com",
  "password": "senha123"
}
```
Retorna: `Bearer eyJhbGciOiJIUzI1NiJ9...`

---

### Usuário

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| GET | `/user?email={email}` | Buscar usuário por email | Sim |
| DELETE | `/user/{email}` | Deletar usuário | Sim |

---

### Tarefas

Todos os endpoints de tarefas requerem o header:
```
Authorization: Bearer {token}
```

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/task` | Criar tarefa |
| GET | `/task` | Listar tarefas do usuário logado |
| DELETE | `/task?id={id}` | Deletar tarefa |

**Exemplo — Criar tarefa:**
```json
POST /task
{
  "name": "Reunião de alinhamento",
  "description": "Reunião semanal com o time",
  "eventAt": "2026-06-01T10:00:00"
}
```

---

## Padrão de Resposta de Erro

```json
{
  "timeStamp": "2026-05-27T14:32:11",
  "status": 404,
  "message": "Usuário não encontrado",
  "path": "/user",
  "erro": "Not Found"
}
```

---

## Testes

```bash
cd back-end
./gradlew test
```

Cobertura:
- Testes unitários de `UserService` e `TaskService` com JUnit 5 + Mockito

---

## CI/CD

O projeto utiliza GitHub Actions para build e testes automáticos a cada push nas branches `main` e `develop`.

O workflow sobe um container PostgreSQL no runner e injeta as variáveis de ambiente necessárias.

---

## Decisões Técnicas e Trade-offs

### JWT Stateless
Optei por autenticação stateless com JWT para manter a API sem estado de sessão, facilitando escalabilidade horizontal. O trade-off é que não há invalidação de token antes da expiração — em produção, seria necessário uma blacklist de tokens.

### MapStruct
Usado para mapeamento entre entidades e DTOs em tempo de compilação, evitando overhead de reflexão em runtime. Alternativa seria ModelMapper, mas MapStruct tem melhor performance.

### PostgreSQL
Optei por PostgreSQL mesmo em desenvolvimento para garantir paridade com o ambiente de produção. O trade-off é a necessidade de ter o banco instalado localmente.

### Relacionamento Bidirecional User-Task
Implementado com `@OneToMany`/`@ManyToOne` bidirecional com `FetchType.LAZY` para evitar queries desnecessárias ao carregar tarefas sem precisar do usuário.

---

## Melhorias Futuras

- Paginação na listagem de tarefas
- Refresh token para renovação automática do JWT
- Edição de tarefas no frontend
- Testes de integração com MockMvc
- Docker Compose para subir o ambiente completo com um comando
- Documentação Swagger/OpenAPI
- Métodos put e patch para atualizar usuário e tarefas
