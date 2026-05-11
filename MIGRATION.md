# Migration Documentation: Roadside Assistance Application

## Decomposition Plan

### Original Monolith Structure
The original application was a single Java 7 class (`AssistantMain.java`) that contained all business logic in a single file with direct JDBC database access using MySQL. The application provided four business capabilities through a console menu:
1. Register Customer
2. Create Breakdown Ticket
3. Assign Mechanic
4. Generate Bill

### Target Microservice Architecture

| Monolith Function | Target Microservice | API Endpoint |
|---|---|---|
| Register Customer (Choice 1) | **customer-service** | POST /api/v1/customers |
| Query Customer | **customer-service** | GET /api/v1/customers/{id} |
| Create Breakdown Ticket (Choice 2) | **ticket-service** | POST /api/v1/tickets |
| Assign Mechanic (Choice 3) | **ticket-service** | PUT /api/v1/tickets/{id}/assign |
| Query Ticket | **ticket-service** | GET /api/v1/tickets/{id} |
| Generate Bill (Choice 4) | **billing-service** | POST /api/v1/billing/generate |
| Query Payments | **billing-service** | GET /api/v1/billing/payments/{ticketId} |

### Bounded Contexts

1. **Customer Domain** → `customer-service`
   - Owns: Customer data (name, vehicle)
   - Database: Isolated customer schema
   - Responsibility: Customer registration and lookup

2. **Ticket Domain** → `ticket-service`
   - Owns: Ticket data (customer reference, status, mechanic assignment)
   - Database: Isolated ticket schema
   - Responsibility: Breakdown ticket lifecycle management

3. **Billing Domain** → `billing-service`
   - Owns: Payment data (ticket reference, amount, timestamp)
   - Database: Isolated billing schema
   - Responsibility: Bill generation and payment tracking

---

## Dependency Report

### Old Dependencies (Monolith)
| Dependency | Version | Purpose |
|---|---|---|
| mysql-connector-java | 8.0.33 | MySQL JDBC Driver |
| Java | 8 (source/target) | Runtime |

### New Dependencies (Microservices)
| Dependency | Version | Purpose |
|---|---|---|
| Spring Boot Starter Parent | 3.2.5 | Framework BOM |
| spring-boot-starter-web | 3.2.5 | REST API |
| spring-boot-starter-data-jpa | 3.2.5 | Data persistence |
| spring-boot-starter-actuator | 3.2.5 | Health/metrics |
| spring-boot-starter-validation | 3.2.5 | Bean validation |
| spring-boot-starter-security | 3.2.5 | Security |
| spring-boot-starter-aop | 3.2.5 | AOP for resilience |
| spring-cloud-function-adapter-aws | 4.1.x | Lambda deployment |
| aws-lambda-java-core | 1.2.3 | Lambda runtime |
| aws-lambda-java-events | 3.11.4 | Lambda event types |
| resilience4j-spring-boot3 | 2.2.0 | Circuit breaker/retry |
| springdoc-openapi-starter-webmvc-ui | 2.5.0 | API documentation |
| logstash-logback-encoder | 7.4 | JSON structured logging |
| h2 | (managed) | Development database |
| Java | 21 | Runtime |

---

## API Boundary Definitions

### Customer Service (Port 8081)
```
POST   /api/v1/customers          - Register a new customer
GET    /api/v1/customers/{id}     - Get customer by ID
GET    /api/v1/customers          - Get all customers
GET    /actuator/health           - Health check
GET    /swagger-ui.html           - API documentation
```

### Ticket Service (Port 8082)
```
POST   /api/v1/tickets            - Create a breakdown ticket
PUT    /api/v1/tickets/{id}/assign - Assign mechanic to ticket
GET    /api/v1/tickets/{id}       - Get ticket by ID
GET    /actuator/health           - Health check
GET    /swagger-ui.html           - API documentation
```

### Billing Service (Port 8083)
```
POST   /api/v1/billing/generate        - Generate a bill
GET    /api/v1/billing/payments/{ticketId} - Get payments for ticket
GET    /actuator/health                 - Health check
GET    /swagger-ui.html                 - API documentation
```

---

## Known Risks and Manual Intervention Points

### High Priority
1. **Database Migration**: The original monolith used a single MySQL database with shared tables. Each microservice now uses its own H2 in-memory database for development. Production deployment requires:
   - Creating separate database schemas/instances per service
   - Migrating existing data from the monolithic database
   - Configuring production database connections via AWS Parameter Store

2. **Data Consistency**: The monolith used a single database transaction for operations spanning customer, ticket, and payment. The microservices architecture requires eventual consistency patterns (saga pattern) for cross-service operations.

3. **Authentication/Authorization**: The current security configuration permits all API requests. Production deployment requires:
   - Configuring an identity provider (e.g., Amazon Cognito)
   - Implementing JWT token validation
   - Defining role-based access control

### Medium Priority
4. **Cross-Service Communication**: The monolith referenced customer data when creating tickets and ticket data when generating bills. The microservices need to call each other via REST APIs or events. This requires implementing:
   - Service-to-service HTTP clients with resilience patterns
   - Or event-driven communication via Amazon EventBridge/SQS

5. **Cold Start Optimization**: Spring Boot applications in AWS Lambda have cold-start latency. Consider:
   - AWS Lambda SnapStart for Java 21
   - GraalVM native image compilation
   - Provisioned concurrency for critical functions

6. **SQL Injection**: The original monolith had SQL injection vulnerabilities (string concatenation in queries). This has been resolved by using Spring Data JPA with parameterized queries.

### Low Priority
7. **Monitoring Setup**: CloudWatch log groups, alarms, and dashboards need to be created for production.
8. **API Gateway Configuration**: Rate limiting, throttling, and API keys should be configured.
9. **Secret Rotation**: AWS Secrets Manager rotation policies should be configured for database credentials.

---

## Refactoring Recommendations

1. **Add Integration Tests**: Each service should have integration tests that verify end-to-end API behavior using Spring Boot Test with `@SpringBootTest` and MockMvc.

2. **Implement Event-Driven Communication**: Replace synchronous cross-service calls with Amazon EventBridge events for loose coupling (e.g., `CustomerRegistered`, `TicketCreated`, `BillGenerated` events).

3. **Add Database Migration Tool**: Use Flyway or Liquibase for database schema versioning and migration management.

4. **Implement API Gateway Authentication**: Configure Amazon Cognito as the authorizer for API Gateway endpoints.

5. **Add Contract Testing**: Use Spring Cloud Contract or Pact to verify API contracts between services.

6. **Consider GraalVM Native Image**: For Lambda cold-start optimization, consider compiling services to native images using GraalVM and Spring Boot's native image support.

7. **Implement Distributed Tracing**: Add AWS X-Ray or OpenTelemetry instrumentation for end-to-end request tracing across services.

8. **Add Rate Limiting**: Implement API rate limiting at the API Gateway level and/or application level for protection against abuse.
