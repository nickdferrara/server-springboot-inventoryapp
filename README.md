# Inventory Management Service

A Spring Boot service for efficiently managing inventory items, tracking stock levels, and handling inventory operations.

## Features

- Product inventory management (create, read, update, delete)
- Stock level tracking
- Inventory movement history
- Low stock alerts
- Warehouse and location management
- User authentication and authorization

## Technology Stack

- Spring Boot 3.x
- Kotlin
- Spring Data JPA
- PostgreSQL
- Spring Security with OAuth 2.0 Resource Server
- Docker / Docker Compose

## Getting Started

### Prerequisites

- JDK 17 or higher
- Gradle
- Docker and Docker Compose (for local development with containerized database)

### Running Locally

1. Clone the repository
2. Run with Docker Compose: `docker-compose up`
3. Alternatively, run only the PostgreSQL container and then run the application manually

### API Documentation

The API documentation will be available via Swagger at `/swagger-ui.html` once the application is running.

## Project Structure

The project follows a standard Spring Boot application structure with controllers, services, repositories, and models.
