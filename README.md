# 🛒 E-Commerce Microservices Platform

A scalable, production-ready e-commerce backend system built with microservices architecture, featuring four independent services that communicate through Apache Kafka for event-driven operations. Designed to handle enterprise-level e-commerce operations similar to Amazon or Trendyol.

## 📋 Table of Contents

- [Architecture Overview](https://github.com/AmrahIbadzada/E-Commerce-Backend-System?tab=readme-ov-file#%EF%B8%8F-architecture-overview)
- [Microservices](#-microservices)
- [Technology Stack](#-technology-stack)
- [Getting Started](https://github.com/AmrahIbadzada/E-Commerce-Backend-System?tab=readme-ov-file#%EF%B8%8Fgetting-started)
- [Docker Operations](#-docker-operations)
- [Configuration](#-configuration)
- [Development](#-development)
- [Monitoring](#-monitoring)
- [API Documentation](#-api-documentation)
- [Contributing](#-contributing)

## 🏗️ Architecture Overview

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
│   :8080    │    │   :8082    │ │   :8084    │ │   :8086     │
└───┬────────┘    └───┬────────┘ └──┬─────────┘ └──┬──────────┘
    │                 │              │              │
    └─────────────────┴──────────────┴──────────────┘
                      │
              ┌───────▼────────┐
              │  Apache Kafka  │
              │    :9092       │
              └───────┬────────┘
                      │
              ┌───────▼────────┐
              │   Zookeeper    │
              │    :2181       │
              └────────────────┘
```

### Key Features

- **Event-Driven Architecture** - Asynchronous communication via Apache Kafka
- **Scalable Design** - Each service can scale independently
- **Fault Tolerance** - Services continue operating if others fail
- **Docker Support** - Full containerization with Docker Compose
- **Production Ready** - Health checks, monitoring, and logging

## 🔧 Microservices

### 1) User Management Service (`user-management-ms`)
**Port:** `8080`

Handles all user-related operations including authentication and authorization.

**Responsibilities:**
- User registration and authentication
- JWT token generation and validation
- Role-based access control (RBAC)
- User profile management
- Password encryption and security

**Key Events Published:**
- `USER_REGISTERED` - New user created
- `USER_UPDATED` - User profile modified
- `USER_DELETED` - User account removed

---

### 2) Product Service (`myproduct-ms`)
**Port:** `8082`

Manages the complete product catalog and inventory system.

**Responsibilities:**
- Product catalog management (CRUD operations)
- Inventory tracking and stock management
- Category and brand management
- Product search and filtering
- Price management

**Key Events Published:**
- `PRODUCT_CREATED` - New product added
- `PRODUCT_UPDATED` - Product information changed
- `INVENTORY_UPDATED` - Stock levels changed
- `PRODUCT_DELETED` - Product removed from catalog

---

### 3) Payment Service (`mypayment-ms`)
**Port:** `8084`

Handles all payment processing and financial transactions.

**Responsibilities:**
- Payment processing and validation
- Transaction management
- Order confirmation and tracking
- Payment gateway integration
- Refund and cancellation handling

**Key Events Published:**
- `PAYMENT_INITIATED` - Payment process started
- `PAYMENT_COMPLETED` - Payment successful
- `PAYMENT_FAILED` - Payment declined or failed
- `REFUND_PROCESSED` - Refund completed

---

### 4) Notification Service (`mynotification-service-ms`)
**Port:** `8086`

Manages all notification delivery across multiple channels.

**Responsibilities:**
- Email notification delivery
- SMS notifications (optional integration)
- Push notifications
- Event-driven message processing
- Notification history and tracking

**Key Events Consumed:**
- All events from other services for notification triggers
- Sends notifications for user actions, order updates, payment status

## 💻 Technology Stack

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
- **PostgreSQL / MySQL** - Relational database management
- **Spring Data JPA** - Data access abstraction
- **Hibernate** - Object-relational mapping
- **Liquibase** - Database schema version control and migration (3 services)

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

## ⚙️Getting Started

### Prerequisites

Ensure you have the following installed on your system:

- **Docker Engine:** Version 20.10 or higher
- **Docker Compose:** Version 2.0 or higher
- **RAM:** Minimum 4-8GB recommended (4GB might work for basic testing)
- **Disk Space:** At least 10GB free space
- **Ports Available:** 8080,8082,8084, 8086, 2181, 9092

Verify your installations:
```bash
docker --version
docker-compose --version
```

### Quick Start

1. **Clone the repository**
```bash
git clone https://github.com/AmrahIbadzada/E-Commerce-Backend-System.git
cd E-Commerce-Backend-System
```

2. **Start all services**
```bash
docker-compose up
```

This command will:
- Pull required Docker images (Kafka, Zookeeper, etc.)
- Build all microservices from source
- Start Kafka, Zookeeper, and all microservices
- Display real-time logs from all containers

**Expected Output:**
```
Creating network "e-commerce-backend-system_default" with the default driver
Creating zookeeper ... done
Creating kafka     ... done
Creating user-management-ms ... done
Creating myproduct-ms ... done
Creating mypayment-ms ... done
Creating mynotification-service-ms ... done
```

3. **Verify all services are running**
```bash
docker-compose ps
```

All services should show status as "Up".

## 🐳 Docker Operations

### Starting Services

```bash
# Start services and view logs in real-time (foreground)
docker-compose up

# Start services in detached mode - runs in background
docker-compose up -d

# Start with rebuilding images (use after code changes)
docker-compose up --build

# Start only specific services
docker-compose up user-management-ms myproduct-ms

# Start previously created containers without recreating them
docker-compose start
```

### Stopping Services

```bash
# Stop running containers gracefully (preserves containers and data)
docker-compose stop

# Stop and remove containers (keeps volumes and persistent data)
docker-compose down

# Stop and remove containers, volumes, and ALL data (complete cleanup)
docker-compose down -v
```

⚠️ **Warning:** `docker-compose down -v` will delete all databases, Kafka topics, and persistent data!

### Useful Commands

```bash
# View logs of all services
docker-compose logs

# View logs of a specific service
docker-compose logs user-management-ms

# Follow logs in real-time (like tail -f)
docker-compose logs -f

# View last 50 lines of logs
docker-compose logs --tail=50

# Check service status
docker-compose ps

# Check detailed service information
docker-compose ps -a

# Restart a specific service
docker-compose restart myproduct-ms

# Restart all services
docker-compose restart

# Rebuild services after code changes
docker-compose up --build

# Execute command inside a running container
docker-compose exec user-management-ms bash

# View resource usage (CPU, Memory, Network)
docker stats

# Remove stopped containers
docker-compose rm
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

### Example API Calls

```bash
# Register a new user
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","email":"john@example.com","password":"pass123"}'

# Get all products
curl http://localhost:8082/api/products

# Process a payment
curl -X POST http://localhost:8084/api/payments/process \
  -H "Content-Type: application/json" \
  -d '{"amount":99.99,"currency":"USD","orderId":"ORD123"}'
```

## 🔧 Configuration

### Environment Variables

Each microservice can be configured through environment variables in `docker-compose.yml`:

```yaml
environment:
  - SPRING_PROFILES_ACTIVE=docker
  - KAFKA_BOOTSTRAP_SERVERS=kafka:9092
  - DATABASE_URL=jdbc:postgresql://postgres:5432/dbname
  - DATABASE_USERNAME=admin
  - DATABASE_PASSWORD=password
  - JWT_SECRET=your_secret_key_here
  - JWT_EXPIRATION=86400000
```

### Kafka Topics

The services communicate through the following Kafka topic:
            | mypayment-ms |
              | Topic |
              |-------|
       | `kafka-payment-topic` | 

### Custom Configuration

To modify service configuration:

1. Edit `docker-compose.yml` for environment variables
2. Modify `application.yml` in each service's `src/main/resources/`
3. Rebuild services: `docker-compose up --build`

## 📑 Development

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
# Step 1: Start only infrastructure (Kafka, Zookeeper, databases)
docker-compose up kafka zookeeper postgres -d

# Step 2: Run microservices locally with your IDE or:
cd user-management-ms
./gradlew bootRun

# Or use Spring DevTools for hot reload
./gradlew bootRun --continuous
```

### Local Development Without Docker

**Prerequisites:**
- Java 17+
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

# Kafka monitoring
docker-compose exec kafka kafka-topics.sh --list --bootstrap-server localhost:9092
docker-compose exec kafka kafka-consumer-groups.sh --list --bootstrap-server localhost:9092
```

## 🐛 Troubleshooting

### Common Issues

#### 1. Services won't start

**Problem:** Ports already in use

```bash
# Check which process is using the port
netstat -tulpn | grep LISTEN  # Linux/Mac
netstat -ano | findstr LISTENING  # Windows

# Kill the process or change port in docker-compose.yml
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

```bash
# Ensure Kafka and Zookeeper are healthy
docker-compose logs kafka
docker-compose logs zookeeper

# Check if Kafka is accepting connections
docker-compose exec kafka kafka-broker-api-versions.sh --bootstrap-server localhost:9092

# Restart Kafka services
docker-compose restart kafka zookeeper

# If issues persist, recreate containers
docker-compose down -v
docker-compose up kafka zookeeper -d
# Wait 30 seconds for Kafka to initialize
docker-compose up myuser-management-ms myproduct-ms mypayment-ms mynotification-service-ms
```

#### 3. Database connection errors

```bash
# Check database logs
docker-compose logs postgres

# Access database directly
docker-compose exec postgres psql -U admin -d ecommerce

# Reset database
docker-compose down -v
docker-compose up postgres -d
```

#### 4. Out of memory errors

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

#### 5. Service dependency issues

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

## 📝 API Documentation

API documentation is available via Swagger UI when services are running:

| Service | Swagger UI | Description |
|---------|-----------|-------------|
| User Management | (http://localhost:8080/user-management-ms/swagger-ui/index.html) | Authentication & user endpoints |
| Product Service | (http://localhost:8082/product-ms/swagger-ui/index.html) | Product & inventory endpoints |
| Payment Service | (http://localhost:8084/payment-ms/swagger-ui/index.html) | Payment processing endpoints |
| Notification Service | (http://localhost:8086/notification-service-ms/swagger-ui/index.html) | Notification endpoints | -- Dependencies disabled

### API Documentation Features

- **Interactive Testing** - Try API calls directly from the browser
- **Request/Response Examples** - See sample data formats
- **Authentication** - Test with JWT tokens
- **Model Schemas** - View complete data structures

## 🤝 Contributing

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
- ✅ Core microservices implementation
- ✅ Docker containerization
- ✅ Kafka event-driven communication
- ✅ Basic API documentation

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

## 📚 Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Docker Documentation](https://docs.docker.com/)
- [Microservices Patterns](https://microservices.io/patterns/index.html)

---

**⭐ If you find this project helpful, please give it a star!**

**Built with ❤️ for learning and demonstration purposes**

---

## 🔖 Keywords

`microservices` `spring-boot` `java` `kafka` `docker` `e-commerce` `backend` `rest-api` `event-driven` `distributed-systems` `gradle` `zookeeper` `docker-compose` `scalable` `enterprise`
