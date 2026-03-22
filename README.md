# Task Manager API

REST API for managing tasks with Spring Boot.

## Technologies
- Java 21
- Spring Boot
- Spring Data JPA
- H2 / PostgreSQL
- Gradle

## How to Run
1. Clone the repository
2. Configure the database
3. Run:
   ./gradlew bootRun

## Endpoints
- GET /tasks
- GET /tasks/{id}
- POST /tasks
- PUT /tasks/{id}/complete
- PUT /tasks/{id}/title
- PUT /tasks/{id}/extend-time
- DELETE /tasks/{id}

## Creation Example
POST /tasks
{
"title": "Study Spring Boot",
"durationSeconds": 60
}