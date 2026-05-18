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
- **MySQL** — Banco de dados principal
- **H2** — Banco em memória para testes
- **Lombok** — Redução de boilerplate
- **SpringDoc OpenAPI** — Documentação Swagger

## 📋 Pré-requisitos

- Java 21+
- Maven
- MySQL

## ⚙️ Configuração

### Banco de dados

Configure as credenciais do MySQL em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/stackitem
spring.datasource.username=root
spring.datasource.password=suasenha
```

### JWT

Defina as propriedades do token JWT no `application.properties`:

```properties
app.jwt.secret=chave-secreta-de-256-bits-para-assinatura-jwt
app.jwt.expiration=3600000
```

## ▶️ Como executar

```bash
# Compilar
mvn clean compile

# Executar testes
mvn test

# Executar a aplicação
mvn spring-boot:run
```

A aplicação inciará em `http://localhost:8080`.

## 📚 Documentação da API

Com a aplicação rodando, acesse:

| Recurso | URL |
|---|---|
| **Swagger UI** | http://localhost:8080/swagger-ui.html |
| **OpenAPI JSON** | http://localhost:8080/v3/api-docs |

### Endpoints

#### Usuários (`/users`)

| Método | Rota | Descrição |
|---|---|---|
| POST | `/users` | Criar usuário (senha criptografada com BCrypt) |
| POST | `/users/login` | Autenticar e obter token JWT |
| GET | `/users` | Listar todos os usuários |
| GET | `/users/{id}` | Buscar usuário por ID |
| PUT | `/users/{id}` | Atualizar usuário |
| DELETE | `/users/{id}` | Excluir usuário |

#### Produtos (`/products`)

| Método | Rota | Descrição |
|---|---|---|
| POST | `/products` | Criar produto |
| GET | `/products` | Listar todos os produtos |
| GET | `/products/{id}` | Buscar produto por ID |
| PUT | `/products/{id}` | Atualizar produto |
| DELETE | `/products/{id}` | Excluir produto |

#### Tags (`/tags`)

| Método | Rota | Descrição |
|---|---|---|
| POST | `/tags` | Criar tag |
| GET | `/tags` | Listar todas as tags |
| GET | `/tags/{id}` | Buscar tag por ID |
| PUT | `/tags/{id}` | Atualizar tag |
| DELETE | `/tags/{id}` | Excluir tag |

#### Vendas (`/sales`)

| Método | Rota | Descrição |
|---|---|---|
| POST | `/sales` | Criar venda |
| GET | `/sales` | Listar todas as vendas |
| GET | `/sales/{id}` | Buscar venda por ID |
| GET | `/sales/user/{usuarioId}` | Vendas de um usuário |
| PUT | `/sales/{id}` | Atualizar venda |
| DELETE | `/sales/{id}` | Excluir venda |

## 🔐 Segurança

- Senhas criptografadas com **BCrypt** antes de armazenar
- Autenticação via **JWT** (JSON Web Token)
- Endpoint `/users/login` retorna token para acesso aos demais recursos

## 🧪 Testes

```bash
mvn test
```

O projeto utiliza:
- **JUnit 5** — Framework de testes
- **Mockito** — Mocks para testes unitários
- **MockMvc** — Testes de controllers
- **JaCoCo** — Relatório de cobertura de código

## 🔍 Qualidade de Código

O projeto está integrado com **SonarQube** para análise estática de código.  
Consulte o guia em [`docs/sonarqube.md`](docs/sonarqube.md) para instruções de uso.

## 📁 Estrutura do Projeto

```
src/
├── main/java/com/lucas/stackitem/
│   ├── controller/      # Controladores REST
│   ├── dto/             # Objetos de transferência de dados
│   ├── infra/           # Configurações (Swagger, Seeder)
│   ├── model/           # Entidades JPA
│   ├── repository/      # Repositórios JPA
│   ├── security/        # JWT e Security Config
│   └── service/         # Lógica de negócio
└── test/java/com/lucas/stackitem/
    ├── config/          # Configurações de teste
    ├── controller/      # Testes dos controllers
    ├── model/           # Testes dos modelos
    ├── security/        # Testes de segurança
    └── service/         # Testes dos serviços