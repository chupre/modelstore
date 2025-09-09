# ---- Build Stage ----
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Install Maven dependencies (Alpine image doesn't include mvnw prerequisites by default)
RUN apk add --no-cache bash maven

# Copy Maven wrapper & project config first (for caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies
RUN ./mvnw dependency:go-offline -B || mvn dependency:go-offline -B

# Copy source and build
COPY src src
RUN ./mvnw clean package -DskipTests -B || mvn clean package -DskipTests -B

# ---- Runtime Stage ----
FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
