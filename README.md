# 🛒 E-Commerce Microservices Platform

[![Java](https://img.shields.io/badge/Java-21+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Kafka](https://img.shields.io/badge/Apache%20Kafka-3.x-black.svg)](https://kafka.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-enabled-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

A scalable, production-ready e-commerce backend system built with microservices architecture, featuring four independent services that communicate through Apache Kafka for event-driven operations. Designed to handle enterprise-level e-commerce operations similar to Amazon or Trendyol.

## Table of Contents

- [Architecture Overview](#-architecture-overview)
- [Microservices](#-microservices)
- [Technology Stack](#-technology-stack)
- [Getting Started](#-getting-started)
- [Docker Operations](#-docker-operations)
- [Service Endpoints](#-service-endpoints)
- [Configuration](#-configuration)
- [Development](#-development)
- [Monitoring](#-monitoring)
- [Troubleshooting](#-troubleshooting)
- [API Documentation](#-api-documentation)
- [Contributing](#-contributing)

## Architecture Overview

This platform implements a distributed microservices architecture designed to handle enterprise-level e-commerce operations.

```
┌─────────────────────────────────────────────────────────────┐
│                       API Gateway (Future)                   │
└────────────┬────────────────────────────────────────────────┘
             │
    ┌────────┴─────────┬──────────────┬──────────────┐
    │                  │              │              │
┌───▼────────┐    ┌───▼────────┐ ┌──▼─────────┐ ┌──▼──────────┐
│   User     │    │  Product   │ │  Payment   │ │Notification │
│Management  │    │  Service   │ │  Service   │ │  Service    │
│  (MySQL)   │    │(PostgreSQL)│ │(PostgreSQL)│ │(PostgreSQL) │
│   :8080    │    │   :8082    │ │   :8084    │ │   :8086     │
└────────────┘    └────────────┘ └──┬─────────┘ └──┬──────────┘
                                     │              │
      REST API Only                  └──────┬───────┘
                                            │
                                    ┌───────▼────────┐
                                    │  Apache Kafka  │
                                    │ :9092 / :29092 │
                                    └───────┬────────┘
                                            │
                                    ┌───────▼────────┐
                                    │   Zookeeper    │
                                    │     :2182      │
                                    └────────────────┘
```

### Key Features

- **Event-Driven Architecture** - Asynchronous communication via Apache Kafka
- **Scalable Design** - Each service can scale independently
- **Fault Tolerance** - Services continue operating if others fail
- **Docker Support** - Full containerization with Docker Compose
- **Database Versioning** - Liquibase for schema migration and version control
- **Production Ready** - Health checks, monitoring, and logging

## Microservices

### 1️⃣ User Management Service (`user-management-ms`)
**Port:** `8080` | **Context Path:** `/user-management-ms`  
**Database:** MySQL (Port: 3306)

Handles all user-related operations including authentication and authorization.

**Responsibilities:**
- User registration and authentication
- JWT token generation and validation (Access: 15min, Refresh: 30 days)
- Role-based access control (RBAC)
- User profile management
- Password encryption and security

**Technology:**
- Database Migration: Liquibase
- ORM: Hibernate with MySQL8 dialect
- Security: JWT with 256-bit secret keys

**Key Events Published:**
- `USER_REGISTERED` - New user created
- `USER_UPDATED` - User profile modified
- `USER_DELETED` - User account removed

**Note:** This service does NOT use Kafka - events are handled via REST API calls.

---

### 2️⃣ Product Service (`myproduct-ms`)
**Port:** `8082` | **Context Path:** `/product-ms`  
**Database:** PostgreSQL 16 (Port: 5454, DB: productdb)

Manages the complete product catalog and inventory system.

**Responsibilities:**
- Product catalog management (CRUD operations)
- Inventory tracking and stock management
- Category and brand management
- Product search and filtering
- Price management

**Technology:**
- Database Migration: Liquibase
- ORM: Hibernate with PostgreSQL dialect
- Security: JWT authentication

**Key Events Published:**
- `PRODUCT_CREATED` - New product added
- `PRODUCT_UPDATED` - Product information changed
- `INVENTORY_UPDATED` - Stock levels changed
- `PRODUCT_DELETED` - Product removed from catalog

**Note:** This service does NOT use Kafka - events are handled via REST API calls.

---

### 3️⃣ Payment Service (`mypayment-ms`)
**Port:** `8084` | **Context Path:** `/payment-ms`  
**Database:** PostgreSQL 17 (Port: 5456, DB: paymentdb)  
**Message Broker:** ✅ Kafka (Port: 9092) + Zookeeper (Port: 2182)

Handles all payment processing and financial transactions with event-driven architecture.

**Responsibilities:**
- Payment processing and validation
- Transaction management
- Order confirmation and tracking
- Payment gateway integration
- Refund and cancellation handling

**Technology:**
- Database Migration: Liquibase
- ORM: Hibernate with PostgreSQL dialect
- Message Broker: Apache Kafka 7.5.0
- Service Communication: OpenFeign (calls Product Service)
- Security: JWT authentication

**Kafka Configuration:**
- **Producer:** JSON serialization with `acks=all` and 3 retries
- **Consumer Group:** `payment-ms-group`
- **Auto Offset Reset:** earliest
- **Trusted Packages:** `*` (all packages allowed for deserialization)

**Key Events Published to Kafka:**
- `PAYMENT_INITIATED` - Payment process started
- `PAYMENT_COMPLETED` - Payment successful
- `PAYMENT_FAILED` - Payment declined or failed
- `REFUND_PROCESSED` - Refund completed

**Feign Client Integration:**
- Connects to Product Service: `http://localhost:8082/product-ms/user`

---

### 4️⃣ Notification Service (`mynotification-service-ms`)
**Port:** `8086` | **Context Path:** `/notification-service-ms`  
**Database:** PostgreSQL 17 (Port: 5456, DB: paymentdb - shared with Payment)  
**Message Broker:** ✅ Kafka (Port: 9092/29092) + Zookeeper (Port: 2182)

Manages all notification delivery across multiple channels via event-driven architecture.

**Responsibilities:**
- Email notification delivery (Gmail SMTP)
- SMS notifications (optional integration)
- Push notifications
- Event-driven message processing from Kafka
- Notification history and tracking

**Technology:**
- Email Provider: Gmail SMTP (smtp.gmail.com:587)
- Message Broker: Apache Kafka 7.5.0
- Network: Custom bridge network (payment-network)

**Kafka Configuration:**
- **Consumer Group:** `notification-group`
- **Auto Offset Reset:** earliest
- **Auto Commit:** Disabled (manual acknowledgment)
- **ACK Mode:** Manual
- **Trusted Packages:** `*` (all packages allowed)

**Key Events Consumed from Kafka:**
- Payment events from Payment Service
- User events (via API gateway in future)
- Order events (via API gateway in future)

**Email Configuration:**
- SMTP Authentication: Enabled
- STARTTLS: Enabled
- Port: 587 (TLS)

**Note:** This service listens to Kafka topics and sends notifications based on incoming events.

## Technology Stack

### Core Technologies
- **Java 21+** - Modern LTS version with enhanced features
- **Spring Boot 4.x** - Enterprise application framework
- **Spring Cloud** - Microservices infrastructure
- **Gradle** - Build automation and dependency management

### Communication & Messaging
- **Apache Kafka** - Distributed event streaming platform
- **Apache Zookeeper** - Distributed coordination service
- **REST API** - Synchronous HTTP communication

### Data & Persistence
- **MySQL** - User Management Service database
- **PostgreSQL** - Product, Payment, and Notification services database
- **Spring Data JPA** - Data access abstraction
- **Hibernate** - Object-relational mapping
- **Liquibase** - Database schema version control and migration (User, Product, Payment services)

### Security
- **Spring Security** - Comprehensive security framework
- **JWT (JSON Web Tokens)** - Stateless authentication
- **BCrypt** - Strong password hashing

### DevOps & Deployment
- **Docker** - Application containerization
- **Docker Compose** - Multi-container orchestration
- **Spring Boot Actuator** - Production-ready monitoring

### Testing & Quality
- **JUnit 5** - Unit testing framework
- **Mockito** - Mocking and stubbing
- **Swagger/OpenAPI** - API documentation

## Getting Started

### Prerequisites

Ensure you have the following installed on your system:

- **Docker Engine:** Version 20.10 or higher
- **Docker Compose:** Version 2.0 or higher
- **RAM:** Minimum 8GB recommended (4GB might work for basic testing)
- **Disk Space:** At least 10GB free space
- **Ports Available:** 8080-8083, 2181, 9092

Verify your installations:
```bash
docker --version
docker-compose --version
```

### Quick Start

**Important:** Each service has its own dependencies managed by separate `docker-compose.yml` files.

1. **Clone the repository**
```bash
git clone https://github.com/AmrahIbadzada/E-Commerce-Backend-System.git
cd E-Commerce-Backend-System
```

2. **Start each service with its dependencies**

```bash
# User Management Service (MySQL)
cd user-management-ms
docker-compose up -d
cd ..

# Product Service (PostgreSQL - Port 5454)
cd myproduct-ms
docker-compose up -d
cd ..

# Payment Service (PostgreSQL + Kafka + Zookeeper)
cd mypayment-ms
docker-compose up -d
cd ..

# Notification Service (Kafka + Zookeeper with shared network)
cd mynotification-service-ms
docker-compose up -d
cd ..
```

**What each command does:**
- Pulls required Docker images (PostgreSQL, MySQL, Kafka, Zookeeper)
- Creates necessary networks and volumes
- Starts databases and message brokers
- Builds and starts the microservice
- Runs Liquibase migrations automatically

3. **Verify all services are running**
```bash
# Check all Docker containers
docker ps

# Should see containers like:
# - product_postgres (port 5454)
# - payment_postgres (port 5456)
# - kafkaCb (port 9092)
# - zookeeperCb (port 2182)
# - And more...
```

4. **Test the services**
```bash
# Check User Management Service
curl http://localhost:8080/user-management-ms/actuator/health

# Check Product Service
curl http://localhost:8082/product-ms/actuator/health

# Check Payment Service
curl http://localhost:8084/payment-ms/actuator/health

# Check Notification Service
curl http://localhost:8086/notification-service-ms/actuator/health
```

All services should return `{"status":"UP"}`

## 🐳 Docker Operations

### Docker Compose Structure

**Important:** Each microservice has its own `docker-compose.yml` file with its dependencies:

```
E-Commerce-Backend-System/
├── user-management-ms/
│   └── docker-compose.yml        # MySQL only
├── myproduct-ms/
│   └── docker-compose.yml        # PostgreSQL only (port 5454)
├── mypayment-ms/
│   └── docker-compose.yml        # PostgreSQL + Kafka + Zookeeper
└── mynotification-service-ms/
    └── docker-compose.yml        # Kafka + Zookeeper (shared network)
```

**Why separate compose files?**
- Each service manages its own dependencies
- Services can be developed and deployed independently
- Easier to scale individual services
- True microservices architecture

### Starting Services

**Option 1: Start All Services Individually**

```bash
# Start User Management Service (MySQL)
cd user-management-ms
docker-compose up -d
cd ..

# Start Product Service (PostgreSQL)
cd myproduct-ms
docker-compose up -d
cd ..

# Start Payment Service (PostgreSQL + Kafka + Zookeeper)
cd mypayment-ms
docker-compose up -d
cd ..

# Start Notification Service (Kafka + Zookeeper)
cd mynotification-service-ms
docker-compose up -d
cd ..
```

**Option 2: Start with Logs (Foreground)**

```bash
# Start Payment service and view logs
cd mypayment-ms
docker-compose up

# In another terminal, start other services
cd myproduct-ms
docker-compose up -d
```

**Common Commands for Any Service:**

```bash
# Navigate to service directory first
cd <service-directory>

# Start services and view logs in real-time (foreground)
docker-compose up

# Start services in detached mode - runs in background
docker-compose up -d

# Start with rebuilding images (use after code changes)
docker-compose up --build

# Start previously created containers without recreating them
docker-compose start
```

### Stopping Services

```bash
# Stop services in current directory
docker-compose stop

# Stop and remove containers (keeps volumes and persistent data)
docker-compose down

# Stop and remove containers, volumes, and ALL data (complete cleanup)
docker-compose down -v
```

⚠️ **Warning:** `docker-compose down -v` will delete all databases, Kafka topics, and persistent data!

**Stop All Services (from root directory):**

```bash
# Stop all services one by one
cd mypayment-ms && docker-compose down && cd ..
cd myproduct-ms && docker-compose down && cd ..
cd user-management-ms && docker-compose down && cd ..
cd mynotification-service-ms && docker-compose down && cd ..

# Or use a script (create stop-all.sh):
#!/bin/bash
for service in user-management-ms myproduct-ms mypayment-ms mynotification-service-ms
do
    cd $service && docker-compose down && cd ..
done
```

### Useful Commands

**All commands should be run from the service directory (e.g., `cd mypayment-ms` first)**

```bash
# View logs of all services in current directory
docker-compose logs

# View logs of a specific container
docker-compose logs payment_postgres
docker-compose logs kafkaCb

# Follow logs in real-time (like tail -f)
docker-compose logs -f

# View last 50 lines of logs
docker-compose logs --tail=50

# Check service status in current directory
docker-compose ps

# Check detailed service information
docker-compose ps -a

# Restart a specific service
docker-compose restart postgres
docker-compose restart kafka

# Restart all services in directory
docker-compose restart

# Rebuild services after code changes
docker-compose up --build

# Execute command inside a running container
docker-compose exec postgres psql -U defaultuser -d paymentdb

# View resource usage (CPU, Memory, Network) - all containers
docker stats

# Remove stopped containers
docker-compose rm
```

**View All Running Containers (Global):**

```bash
# See all containers from all services
docker ps

# See all containers including stopped ones
docker ps -a

# Filter by name
docker ps --filter "name=payment"
```

### Advanced Operations

```bash
# Scale a service to multiple instances
docker-compose up -d --scale myproduct-ms=3

# View compose file configuration
docker-compose config

# Validate compose file without starting services
docker-compose config --quiet

# Pull latest images without starting
docker-compose pull

# Build images without starting services
docker-compose build
```

## Service Endpoints

Once running, services are available at:

| Service | Port | Context Path | Health Check | Base API |
|---------|------|-------------|--------------|----------|
| User Management | 8080 | `/user-management-ms` | http://localhost:8080/user-management-ms/actuator/health | http://localhost:8080/user-management-ms/api |
| Product Service | 8082 | `/product-ms` | http://localhost:8082/product-ms/actuator/health | http://localhost:8082/product-ms/api |
| Payment Service | 8084 | `/payment-ms` | http://localhost:8084/payment-ms/actuator/health | http://localhost:8084/payment-ms/api |
| Notification Service | 8086 | `/notification-service-ms` | http://localhost:8086/notification-service-ms/actuator/health | http://localhost:8086/notification-service-ms/api |

### Infrastructure Ports

| Service | Port(s) | Access |
|---------|--------|--------|
| MySQL (User Management) | 3306 | localhost:3306 |
| PostgreSQL (Product) | 5454 | localhost:5454 |
| PostgreSQL (Payment + Notification) | 5456 | localhost:5456 |
| Kafka | 9092, 29092 | localhost:9092 (external), kafka:29092 (internal) |
| Zookeeper | 2182 | localhost:2182 |

### Example API Calls

```bash
# Register a new user
curl -X POST http://localhost:8080/user-management-ms/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","email":"john@example.com","password":"pass123"}'

# Get all products
curl http://localhost:8082/product-ms/api/products

# Process a payment
curl -X POST http://localhost:8084/payment-ms/api/payments/process \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"amount":99.99,"currency":"USD","orderId":"ORD123"}'

# Send notification (triggered by Kafka events automatically)
# Notification service listens to Kafka topics - no direct API call needed
```

##  Service Endpoints

Once running, services are available at:

| Service | Port | Context Path | Health Check | Base API |
|---------|------|-------------|--------------|----------|
| User Management | 8080 | `/user-management-ms` | http://localhost:8080/user-management-ms/actuator/health | http://localhost:8080/user-management-ms/api |
| Product Service | 8082 | `/product-ms` | http://localhost:8082/product-ms/actuator/health | http://localhost:8082/product-ms/api |
| Payment Service | 8084 | `/payment-ms` | http://localhost:8084/payment-ms/actuator/health | http://localhost:8084/payment-ms/api |
| Notification Service | 8086 | `/notification-service-ms` | http://localhost:8086/notification-service-ms/actuator/health | http://localhost:8086/notification-service-ms/api |

### Infrastructure Ports

| Service | Port(s) | Access |
|---------|--------|--------|
| MySQL (User Management) | 3306 | localhost:3306 |
| PostgreSQL (Product) | 5454 | localhost:5454 |
| PostgreSQL (Payment + Notification) | 5456 | localhost:5456 |
| Kafka | 9092, 29092 | localhost:9092 (external), kafka:29092 (internal) |
| Zookeeper | 2182 | localhost:2182 |

### Example API Calls

```bash
# Register a new user
curl -X POST http://localhost:8080/user-management-ms/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","email":"john@example.com","password":"pass123"}'

# Get all products
curl http://localhost:8082/product-ms/api/products

# Process a payment
curl -X POST http://localhost:8084/payment-ms/api/payments/process \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"amount":99.99,"currency":"USD","orderId":"ORD123"}'

# Send notification (triggered by Kafka events automatically)
# Notification service listens to Kafka topics - no direct API call needed
```

## 🔧 Configuration

### Environment Variables

Each microservice can be configured through environment variables in `docker-compose.yml` or `application.yml`:

#### User Management Service (MySQL)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/userdb
    username: ${DB_USERNAME:defaultuser}
    password: ${DB_PASSWORD:defaultpass}
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: none
  liquibase:
    enabled: true
    change-log: classpath:db/changelog/db-changelog.yaml
server:
  port: 8080
  servlet:
    context-path: /user-management-ms
jwt:
  access-token:
    secret: ${JWT_SECRET:a-string-secret-at-least-256-bits-long}
    expiration-minutes: 15
  refresh-token:
    token-length: 64
    expiration-days: 30
```

#### Product Service (PostgreSQL)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5454/productdb
    username: ${DB_USERNAME:defaultuser}
    password: ${DB_PASSWORD:defaultpass}
  liquibase:
    enabled: true
    change-log: classpath:db/changelog/db.changelog-master.yaml
server:
  port: 8082
  servlet:
    context-path: /product-ms
jwt:
  access-token:
    secret: ${JWT_SECRET:a-string-secret-at-least-256-bits-long}
```

#### Payment Service (PostgreSQL + Kafka)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5456/paymentdb
    username: ${DB_USERNAME:defaultuser}
    password: ${DB_PASSWORD:defaultpass}
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      acks: all
      retries: 3
    consumer:
      group-id: payment-ms-group
      auto-offset-reset: earliest
server:
  port: 8084
  servlet:
    context-path: /payment-ms
feign:
  client:
    product-service:
      url: http://localhost:8082/product-ms/user
```

#### Notification Service (Kafka + Email)
```yaml
spring:
  kafka:
    bootstrap-servers: ${SPRING_KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
    consumer:
      group-id: notification-group
      auto-offset-reset: earliest
      enable-auto-commit: false
    listener:
      ack-mode: manual
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${GMAIL_USERNAME:your-gmail-address}
    password: ${GMAIL_PASSWORD:your-gmail-password}
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true
server:
  port: 8086
  servlet:
    context-path: /notification-service-ms
```

**Security Best Practices:**
- Never commit actual passwords or secrets to Git
- Use environment variables for sensitive data
- All secrets shown above are placeholder values
- Update JWT secrets to strong 256-bit keys in production

### Kafka Topics

**Important:** Only Payment and Notification services use Kafka. User Management and Product services use REST API for communication.

The Payment and Notification services communicate through the following Kafka topics:

| Topic | Purpose | Producer | Consumer | Status |
|-------|---------|----------|----------|--------|
| `payment-events` | Payment lifecycle events | Payment MS | Notification MS | ✅ Active |
| `payment-initiated` | Payment started | Payment MS | Notification MS | ✅ Active |
| `payment-completed` | Payment successful | Payment MS | Notification MS | ✅ Active |
| `payment-failed` | Payment errors | Payment MS | Notification MS | ✅ Active |
| `refund-processed` | Refund completed | Payment MS | Notification MS | ✅ Active |

**Kafka Consumer Groups:**
- Payment Service: `payment-ms-group`
- Notification Service: `notification-group`

**Communication Patterns:**
- **User ↔ Product:** Direct REST API calls
- **Payment → Notification:** Kafka events (asynchronous)
- **Payment → Product:** OpenFeign REST client
- **Product → User:** Direct REST API calls

### Custom Configuration

To modify service configuration:

1. Edit `docker-compose.yml` for environment variables
2. Modify `application.yml` in each service's `src/main/resources/`
3. Rebuild services: `docker-compose up --build`

## 📑 Development

### Database Migrations

This project uses **Liquibase** for database schema versioning and migration management in **3 services** (User Management, Product, Payment).

**Services using Liquibase:**
- ✅ User Management (MySQL)
- ✅ Product Service (PostgreSQL)
- ✅ Payment Service (PostgreSQL)
- ❌ Notification Service (No migration - shared DB with Payment)

**Running migrations:**
```bash
# Migrations run automatically on service startup
docker-compose up

# View migration status for User Management (MySQL)
./gradlew :user-management-ms:liquibaseStatus

# View migration status for Product Service (PostgreSQL)
./gradlew :myproduct-ms:liquibaseStatus

# View migration status for Payment Service (PostgreSQL)
./gradlew :mypayment-ms:liquibaseStatus

# Generate new changelog (example for Product service)
./gradlew :myproduct-ms:liquibaseDiffChangeLog

# Rollback last migration
./gradlew :user-management-ms:liquibaseRollbackCount -PliquibaseCommandValue=1
```

**Migration files location:**
- User Management: `src/main/resources/db/changelog/db-changelog.yaml`
- Product Service: `src/main/resources/db/changelog/db.changelog-master.yaml`
- Payment Service: `src/main/resources/db/changelog/db.changelog-master.yaml`

**Liquibase Tables:**
- `DATABASECHANGELOG` - Tracks executed changesets
- `DATABASECHANGELOGLOCK` - Prevents concurrent migrations

### Building Services Locally

```bash
# Build all services
./gradlew build

# Build specific service
./gradlew :user-management-ms:build

# Run tests for all services
./gradlew test

# Run tests for specific service
./gradlew :user-management-ms:test

# Clean build
./gradlew clean build

# Skip tests (faster build)
./gradlew build -x test
```

### Hot Reload During Development

For development with automatic reload without Docker:

```bash
# Step 1: Start only infrastructure services
# For User Management (MySQL only)
docker-compose up mysql -d

# For Product Service (PostgreSQL only)
docker-compose -f product-ms/docker-compose.yml up postgres -d

# For Payment Service (PostgreSQL + Kafka + Zookeeper)
docker-compose -f payment-ms/docker-compose.yml up postgres kafka zookeeper -d

# For Notification Service (Kafka + Zookeeper - shared with Payment)
docker-compose -f notification-service-ms/docker-compose.yml up kafka zookeeper -d

# Step 2: Run microservices locally with your IDE or:
cd user-management-ms
./gradlew bootRun

# Or use Spring DevTools for hot reload
./gradlew bootRun --continuous
```

**Note:** Each service has its own `docker-compose.yml` for infrastructure dependencies.

### Local Development Without Docker

**Prerequisites:**
- Java 21+
- Gradle 7+
- PostgreSQL or MySQL running locally
- Kafka and Zookeeper running locally

**Steps:**
1. Start Kafka and Zookeeper locally
2. Create required databases
3. Update `application-local.yml` with local connection details
4. Run each service: `./gradlew :service-name:bootRun`

## 📶 Monitoring

### Viewing Container Resources

```bash
# View resource usage (CPU, Memory, Network I/O)
docker stats

# View stats for specific container
docker stats user-management-ms

# View container details
docker inspect user-management-ms

# View network connections
docker network ls
docker network inspect e-commerce-backend-system_default
```

### Application Monitoring

```bash
# Health check endpoints
curl http://localhost:8080/user-management-ms/actuator/health
curl http://localhost:8082/product-ms/actuator/health
curl http://localhost:8084/payment-ms/actuator/health
curl http://localhost:8086/notification-service-ms/actuator/health

# View metrics (if enabled)
curl http://localhost:8080/user-management-ms/actuator/metrics
curl http://localhost:8084/payment-ms/actuator/metrics

# Kafka monitoring (from Payment or Notification service directory)
cd mypayment-ms
docker-compose exec kafkaCb kafka-topics.sh --list --bootstrap-server localhost:9092

# List consumer groups
docker-compose exec kafkaCb kafka-consumer-groups.sh --list --bootstrap-server localhost:9092

# Check specific consumer group
docker-compose exec kafkaCb kafka-consumer-groups.sh \
  --bootstrap-server localhost:9092 \
  --group payment-ms-group \
  --describe

# View messages in a topic (from beginning)
docker-compose exec kafkaCb kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic payment-events \
  --from-beginning
```

### Database Monitoring

```bash
# Connect to MySQL (User Management)
docker exec -it <mysql-container-name> mysql -u defaultuser -p

# Connect to PostgreSQL (Product Service - Port 5454)
docker exec -it product_postgres psql -U defaultuser -d productdb

# Connect to PostgreSQL (Payment Service - Port 5456)
docker exec -it payment_postgres psql -U defaultuser -d paymentdb

# List databases
docker exec -it product_postgres psql -U defaultuser -c "\l"

# Check Liquibase migration history
docker exec -it product_postgres psql -U defaultuser -d productdb \
  -c "SELECT * FROM DATABASECHANGELOG ORDER BY dateexecuted DESC LIMIT 10;"
```

## Troubleshooting

### Common Issues

#### 1. Services won't start

**Problem:** Ports already in use

```bash
# Check which processes are using the ports
# Application ports: 8080, 8082, 8084, 8086
# Database ports: 3306 (MySQL), 5454, 5456 (PostgreSQL)
# Kafka/Zookeeper ports: 9092, 29092, 2182

# Linux/Mac
netstat -tulpn | grep LISTEN
lsof -i :8080  # Check specific port

# Windows
netstat -ano | findstr LISTENING
netstat -ano | findstr :8080

# Kill the process using the port (Linux/Mac)
kill -9 <PID>

# Or change port in docker-compose.yml
ports:
  - "8090:8080"  # Map to different host port
```

**Problem:** Insufficient memory

```bash
# Increase Docker memory limit
# Docker Desktop: Settings > Resources > Memory > 8GB

# Or limit service memory in docker-compose.yml
services:
  user-management-ms:
    mem_limit: 512m
```

**Solution:**
```bash
# Clean up and restart
docker-compose down -v
docker system prune -a
docker-compose up --build
```

#### 2. Kafka connection errors

**Note:** Only Payment and Notification services use Kafka.

```bash
# Navigate to service with Kafka
cd mypayment-ms  # or cd mynotification-service-ms

# Check Kafka logs
docker-compose logs kafkaCb

# Check Zookeeper
docker-compose logs zookeeperCb

# Check if Kafka is accepting connections
docker-compose exec kafkaCb kafka-broker-api-versions.sh --bootstrap-server localhost:9092

# List all topics
docker-compose exec kafkaCb kafka-topics.sh --list --bootstrap-server localhost:9092

# Restart Kafka services
docker-compose restart kafkaCb zookeeperCb

# If issues persist, recreate containers
docker-compose down -v
docker-compose up kafka zookeeper -d
# Wait 30 seconds for Kafka to initialize
docker-compose up
```

**Common Kafka Issues:**
- Kafka starts before Zookeeper is ready → Wait 10-15 seconds before starting Kafka
- Port conflicts (9092, 2182) → Check if ports are in use
- Consumer group lag → Check with `kafka-consumer-groups.sh --describe`

#### 3. Database connection errors

```bash
# Check database logs for Product Service
cd myproduct-ms
docker-compose logs product_postgres

# Check database logs for Payment Service
cd mypayment-ms
docker-compose logs payment_postgres

# Access Product database directly (port 5454)
docker exec -it product_postgres psql -U defaultuser -d productdb

# Access Payment database directly (port 5456)
docker exec -it payment_postgres psql -U defaultuser -d paymentdb

# Verify Liquibase migrations for User Management
./gradlew :user-management-ms:liquibaseStatus

# Verify Liquibase migrations for Product
./gradlew :myproduct-ms:liquibaseStatus

# Verify Liquibase migrations for Payment
./gradlew :mypayment-ms:liquibaseStatus

# Clear Liquibase lock (if stuck) - Product Service example
docker exec -it product_postgres psql -U defaultuser -d productdb \
  -c "UPDATE DATABASECHANGELOGLOCK SET LOCKED=FALSE, LOCKGRANTED=null, LOCKEDBY=null WHERE ID=1;"

# Reset a specific database
cd myproduct-ms
docker-compose down -v
docker-compose up postgres -d
```

**Database Connection Issues:**
- Wrong port: User (3306), Product (5454), Payment/Notification (5456)
- Check if databases are created: `\l` in psql or `SHOW DATABASES;` in mysql
- Verify credentials match docker-compose.yml and application.yml

#### 4. Liquibase migration errors

```bash
# Check migration logs
docker-compose logs user-management-ms | grep liquibase

# Validate changelog files
./gradlew :user-management-ms:liquibaseValidate

# Force release lock
./gradlew :user-management-ms:liquibaseReleaseLocks

# Mark changesets as executed (use carefully)
./gradlew :user-management-ms:liquibaseChangelogSync
```

#### 5. Out of memory errors

```bash
# Check container memory usage
docker stats

# Increase Docker memory in Docker Desktop settings
# Settings > Resources > Advanced > Memory: 8GB+

# Or add memory limits to docker-compose.yml
services:
  user-management-ms:
    deploy:
      resources:
        limits:
          memory: 512M
```

#### 6. Service dependency issues

**Problem:** Services start before Kafka is ready

**Solution:** Add health checks and depends_on in `docker-compose.yml`:

```yaml
services:
  user-management-ms:
    depends_on:
      kafka:
        condition: service_healthy
```

### Debug Mode

```bash
# View detailed logs with timestamps
docker-compose logs -t -f

# Check service health
docker-compose ps

# Enter container for debugging
docker-compose exec user-management-ms bash

# View environment variables
docker-compose exec user-management-ms env

# Check network connectivity between services
docker-compose exec user-management-ms ping myproduct-ms
```

### Reset Everything

If all else fails, perform a complete reset:

```bash
# Stop all containers
docker-compose down -v

# Remove all Docker resources
docker system prune -a --volumes

# Rebuild and start fresh
docker-compose up --build
```

## API Documentation

API documentation is available via Swagger UI when services are running:

| Service | Swagger UI | Description |
|---------|-----------|-------------|
| User Management | http://localhost:8080/user-management-ms/swagger-ui/index.html | Authentication & user endpoints |
| Product Service | http://localhost:8082/product-ms/swagger-ui/index.html | Product & inventory endpoints |
| Payment Service | http://localhost:8084/payment-ms/swagger-ui/index.html | Payment processing endpoints |
| Notification Service | http://localhost:8086/notification-service-ms/swagger-ui/index.html | Notification endpoints (if available) |

### API Documentation Features

- **Interactive Testing** - Try API calls directly from the browser
- **Request/Response Examples** - See sample data formats
- **Authentication** - Test with JWT tokens
- **Model Schemas** - View complete data structures

**Note:** Notification Service primarily consumes Kafka events and may not expose public REST APIs.

## Contributing

Contributions are welcome! Please follow these steps:

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Commit your changes**
   ```bash
   git commit -m 'Add some amazing feature'
   ```
4. **Push to the branch**
   ```bash
   git push origin feature/amazing-feature
   ```
5. **Open a Pull Request**

### Development Guidelines

- Follow Java coding conventions and Spring Boot best practices
- Write unit tests for new features (minimum 80% coverage)
- Update documentation as needed
- Ensure all tests pass before submitting PR
- Use meaningful commit messages (Conventional Commits format)
- Add comments for complex logic

### Code Style

```bash
# Check code style
./gradlew checkstyleMain

# Format code
./gradlew spotlessApply
```

## 📄 License

This project is licensed under the MIT License - see the https://github.com/AmrahIbadzada/E-Commerce-Backend-System/blob/main/LICENSE file for details.

## 👤 Author

**Amrah Ibadzada**

- GitHub: [@AmrahIbadzada](https://github.com/AmrahIbadzada)
- LinkedIn: https://az.linkedin.com/in/amrah-ibadzada/
- Email: emrahibadzade5@gmail.com

## 🌟 Acknowledgments

- Spring Boot team for the excellent framework
- Apache Kafka for event streaming capabilities  
- Docker for containerization platform
- Open source community for inspiration and support

---

## 📈 Project Roadmap

### Current Status
- Core microservices implementation
- Docker containerization
- Kafka event-driven communication
- Basic API documentation

### Planned Features
- [ ] **API Gateway** - Centralized entry point with routing
- [ ] **Service Discovery** - Eureka server for dynamic service registration
- [ ] **Distributed Tracing** - Zipkin/Sleuth for request tracking
- [ ] **Centralized Configuration** - Spring Cloud Config Server
- [ ] **Circuit Breaker** - Resilience4j for fault tolerance
- [ ] **Caching Layer** - Redis for performance optimization
- [ ] **Search Engine** - Elasticsearch for advanced product search
- [ ] **Database Per Service** - Separate databases for each microservice
- [ ] **Message Queue** - Dead letter queue handling
- [ ] **Security Enhancements** - OAuth2, rate limiting
- [ ] **Kubernetes Deployment** - K8s manifests and Helm charts
- [ ] **CI/CD Pipeline** - GitHub Actions for automated testing and deployment
- [ ] **Monitoring Dashboard** - Grafana + Prometheus
- [ ] **Load Testing** - JMeter/Gatling test suites

---

## Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Docker Documentation](https://docs.docker.com/)
- [Microservices Patterns](https://microservices.io/patterns/index.html)

---

**⭐ If you find this project helpful, please give it a star!**

**Built with ❤️ for learning and demonstration purposes**

---

## Keywords

`microservices` `spring-boot` `java` `kafka` `docker` `e-commerce` `backend` `rest-api` `event-driven` `distributed-systems` `gradle` `zookeeper` `docker-compose` `scalable` `enterprise`
