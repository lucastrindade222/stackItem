# ---- Stage 1: Build ----
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copia apenas o pom.xml primeiro para aproveitar cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código fonte e compila
COPY src ./src
RUN mvn package -DskipTests -B

# ---- Stage 2: Run ----
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# Cria usuário não-root para segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copia o JAR do estágio de build
COPY --from=build /app/target/*.jar app.jar

# Define o usuário não-root
USER appuser

# Expõe a porta padrão da aplicação
EXPOSE 8080

# Comando de entrada usando o profile de produção
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]