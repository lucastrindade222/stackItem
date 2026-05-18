 # 🔍 SonarQube - Análise de Qualidade do Código

## Pré-requisitos

- Docker instalado
- Projeto compilado (com `mvn clean install` ou `mvn clean test`)

## Iniciar o SonarQube

```bash
docker run -d --name sonarqube -p 9000:9000 sonarqube:lts
```

Acesse http://localhost:9000 e faça login com:
- **Usuário:** `admin`
- **Senha:** `admin`

> ⚠️ Na primeira vez, o SonarQube solicitará que você altere a senha.

## Executar a Análise

### Opção 1 — Comando completo (recomendado)

```bash
mvn clean test jacoco:report sonar:sonar -Dsonar.login=admin -Dsonar.password=admin
```

### Opção 2 — Passo a passo

```bash
# 1. Rodar os testes e gerar relatório de cobertura (JaCoCo)
mvn clean test jacoco:report

# 2. Executar a análise do SonarQube
mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=admin
```

## Visualizar os Resultados

Após a análise, acesse **http://localhost:9000** e clique no projeto **stackItem**.

Você verá métricas como:
- **Bugs** 🐛 — Erros no código que podem quebrar a aplicação
- **Vulnerabilidades** 🔒 — Falhas de segurança
- **Code Smells** 🧹 — Mau cheiro no código (boas práticas)
- **Cobertura** 📈 — Percentual do código coberto por testes
- **Duplicações** 📑 — Código repetido

## Parar o SonarQube

```bash
docker stop sonarqube
```

## Remover o container

```bash
docker rm sonarqube