# Roadside Assistance - Microservices

A modern cloud-native roadside assistance application built with Java 21, Spring Boot 3.x, and AWS-native deployment patterns. Migrated from a Java 7 monolithic application into domain-oriented microservices.

## Architecture Overview

This application is decomposed into three microservices based on bounded contexts:

```
roadside-assistant/
├── pom.xml                          # Parent POM (Maven multi-module)
├── customer-service/                # Customer management microservice
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/roadside/customer/
│       ├── CustomerServiceApplication.java
│       ├── config/                  # Security, Resilience, Function configs
│       ├── controller/             # REST controllers
│       ├── dto/                    # Java 21 records (DTOs)
│       ├── exception/              # Exception handlers
│       ├── model/                  # JPA entities
│       ├── repository/             # Spring Data JPA repositories
│       └── service/                # Business logic
├── ticket-service/                  # Breakdown ticket management
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/roadside/ticket/
│       └── (same structure as customer-service)
├── billing-service/                 # Billing and payment management
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/roadside/billing/
│       └── (same structure as customer-service)
├── template.yml                     # AWS SAM template
├── buildspec.yml                    # AWS CodeBuild configuration
└── MIGRATION.md                     # Migration documentation
```

## Technology Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Language runtime |
| Spring Boot | 3.2.5 | Application framework |
| Spring Data JPA | 3.2.5 | Data persistence |
| Spring Security | 6.x | Security |
| Spring Cloud Function | 4.1.x | AWS Lambda adapter |
| Resilience4j | 2.2.0 | Circuit breaker, retry, timeout |
| SpringDoc OpenAPI | 2.5.0 | API documentation |
| H2 Database | (managed) | Development database |
| Logstash Logback | 7.4 | JSON structured logging |

## Getting Started

### Prerequisites
- Java 21 (JDK)
- Maven 3.9+
- (Optional) Docker for containerized deployment
- (Optional) AWS CLI and SAM CLI for Lambda deployment

### Build All Services
```bash
mvn clean install
```

### Run Individual Services
```bash
# Customer Service (port 8081)
cd customer-service
mvn spring-boot:run

# Ticket Service (port 8082)
cd ticket-service
mvn spring-boot:run

# Billing Service (port 8083)
cd billing-service
mvn spring-boot:run
```

### Access API Documentation
- Customer Service: http://localhost:8081/swagger-ui.html
- Ticket Service: http://localhost:8082/swagger-ui.html
- Billing Service: http://localhost:8083/swagger-ui.html

### Health Checks
- Customer Service: http://localhost:8081/actuator/health
- Ticket Service: http://localhost:8082/actuator/health
- Billing Service: http://localhost:8083/actuator/health

## API Endpoints

### Customer Service
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/v1/customers | Register a new customer |
| GET | /api/v1/customers/{id} | Get customer by ID |
| GET | /api/v1/customers | Get all customers |

### Ticket Service
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/v1/tickets | Create a breakdown ticket |
| PUT | /api/v1/tickets/{id}/assign | Assign mechanic to ticket |
| GET | /api/v1/tickets/{id} | Get ticket by ID |

### Billing Service
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/v1/billing/generate | Generate a bill |
| GET | /api/v1/billing/payments/{ticketId} | Get payments for ticket |

## AWS Deployment

### Lambda Deployment (via SAM)
```bash
sam build
sam deploy --guided
```

### Container Deployment (via Docker)
```bash
docker build -t customer-service ./customer-service
docker build -t ticket-service ./ticket-service
docker build -t billing-service ./billing-service
```

## Configuration

All services use externalized configuration via environment variables, compatible with AWS Parameter Store and Secrets Manager:

| Variable | Default | Description |
|---|---|---|
| DB_URL | jdbc:h2:mem:*db | Database URL |
| DB_DRIVER | org.h2.Driver | JDBC driver |
| DB_USERNAME | sa | Database username |
| DB_PASSWORD | (empty) | Database password |
| SERVER_PORT | 8081/8082/8083 | Server port |
| AWS_REGION | us-east-1 | AWS region |
| SPRING_LAZY_INIT | false | Lazy initialization (for Lambda) |

## Java 21 Features Used

- **Records** - Immutable DTOs (CustomerDTO, TicketDTO, PaymentDTO, request objects)
- **Switch Expressions** - Pattern matching in exception handlers and status management
- **Optional** - Null safety in service layer methods
- **Streams API** - Collection processing and transformations
- **Text Blocks** - Multi-line string literals
- **var** - Local variable type inference

## Migration Documentation

See [MIGRATION.md](MIGRATION.md) for:
- Decomposition plan mapping monolith to microservices
- Dependency report (old vs new)
- API boundary definitions
- Known risks and manual intervention points
- Refactoring recommendations
