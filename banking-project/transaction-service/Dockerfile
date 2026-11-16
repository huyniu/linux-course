# Build stage: dong goi file JAR bang Gradle wrapper
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
# Cache thu vien Gradle giua cac lan build de build lai nhanh hon
RUN --mount=type=cache,target=/root/.gradle \
    chmod +x gradlew && ./gradlew bootJar -x test --no-daemon -q

# Runtime stage: chi chua JRE va file JAR
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
