# Multi-stage build for Ticket Service
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace/app

COPY ../pom.xml ../pom.xml
COPY pom.xml .
COPY src src

RUN --mount=type=cache,target=/root/.m2 \
    cd .. && mvn clean package -pl ticket-service -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Add non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /workspace/app/target/ticket-service-1.0.0.jar app.jar

# JVM optimization for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+TieredCompilation -XX:TieredStopAtLevel=1"

EXPOSE 8082

HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget -qO- http://localhost:8082/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
