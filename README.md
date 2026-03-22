# Task Manager API

REST API built with Java and Spring Boot for user authentication and task management.

## Features

- User registration and login
- JWT-based authentication
- Protected task endpoints
- Task creation, listing, completion, title update, time extension, and deletion
- PostgreSQL integration
- Docker and Docker Compose support
- Validation and layered architecture

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT
- Gradle
- Docker

## Project Structure

- `controller` → REST endpoints
- `service` → business logic
- `repository` → data access
- `security` → JWT and authentication logic
- `core.request` → request DTOs
- `core.response` → response DTOs
- `core.task` / `core.user` → domain entities and enums

## Environment Variables

Create a local `.env` file based on `.env.example`.

Example:

```env
DB_URL=jdbc:postgresql://localhost:5432/task_manager_api
DB_USERNAME=postgres
DB_PASSWORD=change_me
JWT_SECRET=replace_with_a_new_base64_secret
JWT_EXPIRATION=86400000
SPRING_PROFILES_ACTIVE=dev