# Ultima Dashboard Backend

This is the backend for a dashboard application inspired by the Ultima PrimeNG template, providing RESTful APIs for the **Sales**, **SaaS**, and **Analytics** dashboards. Built with Spring Boot, Java 21, and PostgreSQL, it supports real-time metrics, graphs, and user management with JWT-based authentication.

## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Setup Instructions](#setup-instructions)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [Database Schema](#database-schema)
- [Contributing](#contributing)
- [License](#license)

## Features
- **Sales Dashboard**: Displays orders, revenue, customers, comments, contacts, order graphs, timeline, products, chat, activity, and best sellers.
- **SaaS Dashboard**: Provides metrics for users, subscriptions, revenue, visitors, tasks, revenue graphs, timeline, subscriptions, messages, and activity.
- **Analytics Dashboard**: Offers insights into revenue, potential reach, pageviews, engagement rate, visitor graphs, most visited pages, referrals, devices, audience, and blog posts.
- **JWT Authentication**: Secures endpoints with role-based access (e.g., ADMIN, USER, MANAGER).
- **RESTful APIs**: Exposes endpoints for dashboard data, supporting pagination and filtering where applicable.
- **Scalable Design**: Modular architecture with reusable entities, services, and controllers.

## Tech Stack
- **Language**: Java 21
- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA
- **Security**: Spring Security with JWT
- **Dependencies**: Lombok, Java Records (for DTOs), SLF4J (logging)
- **Build Tool**: Maven

## Project Structure
```
com.tinusj.ultima/
├── controller/           # REST controllers for public API endpoints
│   ├── pub/             # Publicly accessible controllers (e.g., MetricsController)
├── dao/                 # Data Access Objects
│   ├── dto/             # Data Transfer Objects (Java records)
│   ├── entity/          # JPA entities (e.g., User, OrderEntity)
├── repository/          # Spring Data JPA repositories
├── service/             # Business logic interfaces
│   ├── impl/            # Service implementations (e.g., DashboardServiceImpl)
├── exception/           # Custom exceptions (e.g., ResourceNotFoundException)
└── configuration/       # Spring configuration (e.g., SecurityConfig)
```

## Setup Instructions

### Prerequisites
- Java 21 (JDK)
- Maven 3.8+
- PostgreSQL 15+
- IDE (e.g., IntelliJ IDEA, Eclipse)
- Optional: Postman or curl for API testing

### Steps
1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-repo/ultima-dashboard-backend.git
   cd ultima-dashboard-backend
   ```

2. **Configure Database**
    - Create a PostgreSQL database:
      ```sql
      CREATE DATABASE ultima_dashboard;
      ```
    - Update `src/main/resources/application.yml` with your database credentials:
      ```yaml
      spring:
        datasource:
          url: jdbc:postgresql://localhost:5432/ultima_dashboard
          username: your_username
          password: your_password
        jpa:
          hibernate:
            ddl-auto: update
      ```

3. **Install Dependencies**
   ```bash
   mvn clean install
   ```

4. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```
   The application will start on `http://localhost:8080`.

5. **Seed Initial Data**
    - Insert sample users and roles:
      ```sql
      INSERT INTO users (id, username, password) VALUES (1, 'admin', '$2a$10$...'); -- BCrypt password
      INSERT INTO user_roles (user_id, role) VALUES (1, 'ADMIN'), (1, 'USER');
      ```
    - Populate other tables (`orders`, `customers`, etc.) as needed.

6. **Test APIs**
    - Use Postman to test endpoints (e.g., `GET /api/v1/dashboard/metrics`).
    - Obtain a JWT token by authenticating (configure your auth endpoint).

## API Endpoints
All endpoints are prefixed with `/api/v1`.

### Sales Dashboard
- `GET /dashboard/metrics`: Sales metrics (orders, revenue, customers, comments)
- `GET /orders/graph?startDate=YYYY-MM-DD`: Order graph data
- `GET /contacts`: List contacts
- `GET /products`: List products
- `GET /products/best-sellers`: Best-selling products

### SaaS Dashboard
- `GET /saas/metrics`: SaaS metrics (users, subscriptions, revenue, visitors)
- `GET /revenue/graph?startDate=YYYY-MM-DD`: Revenue graph data
- `GET /tasks`: List tasks
- `GET /subscriptions`: List subscriptions
- `GET /visitors`: List visitors

### Analytics Dashboard
- `GET /analytics/metrics?startDate=YYYY-MM-DD`: Analytics metrics (revenue, reach, pageviews, engagement)
- `GET /analytics/visitors/graph?startDate=YYYY-MM-DD`: Visitors graph data
- `GET /analytics/pages`: Most visited pages
- `GET /analytics/referrals`: Referral sources
- `GET /analytics/devices`: Device distribution
- `GET /analytics/audience`: Audience demographics
- `GET /analytics/blog-posts`: Blog posts

### Shared Endpoints
- `GET /timeline`: Timeline events
- `GET /chat`: Chat messages (authenticated)
- `GET /activities`: Activity logs (authenticated)

## Authentication
- **JWT-based**: Authenticate via a login endpoint (not implemented here; assumes external auth service).
- **Roles**: `ADMIN`, `USER`, `MANAGER` (stored as enum in `user_roles` table).
- **Secured Endpoints**: `/chat`, `/activities` require authentication (`@PreAuthorize("isAuthenticated()")`).
- **Role-based Access**: Extend with `@PreAuthorize("hasRole('ADMIN')")` for restricted endpoints.

## Database Schema
Key tables:
- `users`: Stores user credentials and IDs.
- `user_roles`: Maps users to roles (e.g., ADMIN, USER).
- `orders`: Tracks orders with product/subscription/visitor references.
- `customers`: Represents users (Sales/SaaS) and audience (Analytics).
- `visitors`: Stores visitor data (source, count, date, device, page).
- `subscriptions`: SaaS subscription plans.
- `tasks`: SaaS tasks.
- `blog_posts`: Analytics blog posts.
- `products`, `comments`, `contacts`, `timeline_events`, `chat_messages`, `activities`: Support Sales/SaaS features.

Run `spring.jpa.hibernate.ddl-auto=update` to auto-generate tables, or use a migration tool like Flyway.

## Contributing
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/your-feature`).
3. Commit changes (`git commit -m "Add your feature"`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a Pull Request.

Please follow the [Code of Conduct](CODE_OF_CONDUCT.md) and include tests for new features.

## License
This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.