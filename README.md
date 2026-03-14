# Task Manager API

API REST para gestionar tareas con Spring Boot.

## Tecnologías
- Java 21
- Spring Boot
- Spring Data JPA
- H2 / PostgreSQL
- Gradle

## Cómo ejecutar
1. Clona el repositorio
2. Configura la base de datos
3. Ejecuta:
   ./gradlew bootRun

## Endpoints
- GET /tasks
- GET /tasks/{id}
- POST /tasks
- PUT /tasks/{id}/complete
- PUT /tasks/{id}/title
- PUT /tasks/{id}/extend-time
- DELETE /tasks/{id}

## Ejemplo de creación
POST /tasks
{
"title": "Estudiar Spring Boot",
"durationSeconds": 60
}