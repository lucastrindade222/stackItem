# 🏪 StackItem

Sistema de gestão de vendas e produtos com autenticação JWT.

## 🚀 Tecnologias

- **Java 21**
- **Spring Boot 3.2**
  - Spring Data JPA
  - Spring Security
  - Spring Web
  - Spring Validation
- **JWT** (jjwt) — Autenticação stateless
- **MySQL 8** — Banco de dados principal
- **H2** — Banco em memória para testes
- **Lombok** — Redução de boilerplate
- **SpringDoc OpenAPI 2** — Documentação Swagger
- **Docker** — MySQL via container
- **JaCoCo** — Cobertura de testes
- **SonarQube** — Qualidade de código

## 📋 Pré-requisitos

- Java 21+
- Maven
- Docker (para MySQL)

## ⚙️ Configuração

### Banco de dados (via Docker)

O projeto utiliza MySQL 8 rodando em container Docker:

```bash
# Iniciar MySQL
docker run --name mysql_stackitem \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_USER=frodo \
  -e MYSQL_PASSWORD=bolseiro \
  -e MYSQL_DATABASE=stackitem_db \
  -p 3306:3306 \
  -d mysql:8.0
```

### Perfil ativo

A aplicação usa o profile `dev` por padrão (definido em `application.properties`):

```properties
spring.profiles.active=dev
```

### Configurações do MySQL

As credenciais do banco estão em `src/main/resources/application-dev.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/stackitem_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Recife
spring.datasource.username=frodo
spring.datasource.password=bolseiro
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

### JWT

Definido em `application-dev.properties`:

```properties
app.jwt.secret=StackItemSecretKey2024VeryLongAndSecureSecretKeyForJWTTokenGeneration
app.jwt.expiration=86400000   # 24 horas
```

### Resetar banco de dados

Se precisar recriar o banco do zero:

```bash
docker exec -i mysql_stackitem mysql -ufrodo -pbolseiro \
  -e "DROP DATABASE IF EXISTS stackitem_db; CREATE DATABASE stackitem_db;"
```

## ▶️ Como executar

```bash
# Compilar
mvn clean compile

# Executar testes
mvn test

# Executar a aplicação
mvn spring-boot:run

# Executar com relatório de cobertura
mvn verify
```

A aplicação inicia em `http://localhost:8080`.

Todas as requisições devem usar o prefixo `/api` (configurado via `spring.mvc.servlet.path=/api`).

## 📚 Documentação da API (Swagger)

Com a aplicação rodando, acesse:

| Recurso | URL |
|---|---|
| **Swagger UI** | http://localhost:8080/api/swagger-ui.html |
| **OpenAPI JSON** | http://localhost:8080/api/v3/api-docs |

## 🔐 Autenticação

### Login

```bash
curl -s -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"lucas.dev.java@email.com","senha":"senha123"}'
```

Resposta:

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "usuario": {
    "id": 1,
    "nome": "Lucas",
    "email": "lucas.dev.java@email.com",
    "perfil": { "id": 1, "nome": "ADMINISTRADOR" }
  }
}
```

### Usar token em requisições autenticadas

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"lucas.dev.java@email.com","senha":"senha123"}' | jq -r '.token')

curl -s http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN"
```

### Seed de dados (DbSeeder)

No profile `dev`, ao iniciar a aplicação com o banco vazio, o `DbSeeder` cria automaticamente:

1. **Perfil**: `ADMINISTRADOR`
2. **Usuário admin**: `lucas.dev.java@email.com` / `senha123`

## 🔐 Segurança

- Senhas criptografadas com **BCrypt** antes de armazenar
- Autenticação via **JWT** stateless (sem sessão)
- Filtro `JwtAuthenticationFilter` valida token em todas as requisições autenticadas
- Endpoints públicos: `/users/login`, `/users` (POST), Swagger
- CORS configurado para `http://localhost:4200`

## 📋 Endpoints da API

### Usuários (`/api/users`)

| Método | Rota | Autenticação | Descrição |
|--------|------|-------------|-----------|
| POST | `/users` | ❌ | Criar usuário |
| POST | `/users/login` | ❌ | Autenticar e obter token JWT |
| GET | `/users` | ✅ | Listar todos os usuários |
| GET | `/users/{id}` | ✅ | Buscar usuário por ID |
| PUT | `/users/{id}` | ✅ | Atualizar usuário |
| DELETE | `/users/{id}` | ✅ | Excluir usuário |
| POST | `/users/{id}/upload-image` | ✅ | Upload de foto de perfil |

### Produtos (`/api/products`)

| Método | Rota | Autenticação | Descrição |
|--------|------|-------------|-----------|
| POST | `/products` | ✅ | Criar produto |
| GET | `/products` | ✅ | Listar todos os produtos |
| GET | `/products/paged` | ✅ | Listar produtos paginados |
| GET | `/products/{id}` | ✅ | Buscar produto por ID |
| PUT | `/products/{id}` | ✅ | Atualizar produto |
| DELETE | `/products/{id}` | ✅ | Excluir produto |

### Tags (`/api/tags`)

| Método | Rota | Autenticação | Descrição |
|--------|------|-------------|-----------|
| POST | `/tags` | ✅ | Criar tag |
| GET | `/tags` | ✅ | Listar todas as tags |
| GET | `/tags/paged` | ✅ | Listar tags paginadas |
| GET | `/tags/{id}` | ✅ | Buscar tag por ID |
| PUT | `/tags/{id}` | ✅ | Atualizar tag |
| DELETE | `/tags/{id}` | ✅ | Excluir tag |

### Vendas (`/api/sales`)

| Método | Rota | Autenticação | Descrição |
|--------|------|-------------|-----------|
| POST | `/sales` | ✅ | Criar venda |
| GET | `/sales` | ✅ | Listar todas as vendas |
| GET | `/sales/paged` | ✅ | Listar vendas paginadas |
| GET | `/sales/{id}` | ✅ | Buscar venda por ID |
| GET | `/sales/user/{usuarioId}` | ✅ | Vendas de um usuário |
| PUT | `/sales/{id}` | ✅ | Atualizar venda |
| DELETE | `/sales/{id}` | ✅ | Excluir venda |

### Perfis (`/api/perfils`)

| Método | Rota | Autenticação | Descrição |
|--------|------|-------------|-----------|
| POST | `/perfils` | ✅ | Criar perfil |
| GET | `/perfils` | ✅ | Listar todos os perfis |
| GET | `/perfils/{id}` | ✅ | Buscar perfil por ID |
| PUT | `/perfils/{id}` | ✅ | Atualizar perfil |
| DELETE | `/perfils/{id}` | ✅ | Excluir perfil |

## 🧪 Testes

```bash
mvn test
```

O projeto utiliza:
- **JUnit 5** — Framework de testes
- **Mockito** — Mocks para testes unitários
- **MockMvc** — Testes de controllers
- **JaCoCo** — Relatório de cobertura de código

### Relatório de cobertura

```bash
mvn verify
# Abrir relatório:
open target/site/jacoco/index.html
```

## 🔍 Qualidade de Código

O projeto está integrado com **SonarQube** para análise estática de código.
Consulte o guia em [`docs/sonarqube.md`](docs/sonarqube.md) para instruções de uso.

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/lucas/stackitem/
│   │   ├── config/           # Configurações (CORS removido — unificado no SecurityConfig)
│   │   ├── controller/       # Controladores REST
│   │   ├── dto/              # Objetos de transferência de dados (LoginRequest, LoginResponse)
│   │   ├── infra/            # Configurações (OpenApiConfig, DbSeeder)
│   │   ├── model/            # Entidades JPA (Usuario, Perfil, Produto, Tag, Venda, ...)
│   │   ├── repository/       # Repositórios JPA
│   │   ├── security/         # JWT (JwtTokenProvider, JwtAuthenticationFilter, SecurityConfig)
│   │   └── service/          # Lógica de negócio
│   └── resources/
│       ├── application.properties         # Configuração principal
│       ├── application-dev.properties     # Configuração do profile dev
│       └── banner.txt                     # Banner personalizado
└── test/
    ├── java/com/lucas/stackitem/
    │   ├── controller/       # Testes dos controllers
    │   ├── model/            # Testes dos modelos
    │   ├── security/         # Testes de segurança
    │   └── service/          # Testes dos serviços
    └── resources/
        └── application-test.properties    # Configuração de teste (H2)
```

## 🐳 Docker

```bash
# Iniciar MySQL
docker start mysql_stackitem

# Parar MySQL
docker stop mysql_stackitem

# Acessar MySQL via terminal
docker exec -it mysql_stackitem mysql -ufrodo -pbolseiro stackitem_db