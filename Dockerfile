# ---- Build Stage ----
FROM eclipse-temurin:24-jdk-ubi9-minimal AS builder
WORKDIR /app

# Copy Maven wrapper & project config first (for caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source and build
COPY src src
RUN ./mvnw clean package -DskipTests -B

# ---- Runtime Stage ----
FROM sapmachine:24-jre-ubuntu-jammy AS runtime
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]