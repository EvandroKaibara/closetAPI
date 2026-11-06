# closetAPI

REST API for a virtual closet. Spring Boot 4, Java 21, PostgreSQL.

## Run locally

Requirements: JDK 21 and a running PostgreSQL.

```bash
createdb closet
createdb closet_test   # used by the tests
./mvnw spring-boot:run
```

Connection settings come from environment variables, with local defaults:

| Variable | Default |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/closet` |
| `DB_USERNAME` | current OS user |
| `DB_PASSWORD` | empty |
| `DB_TEST_URL` | `jdbc:postgresql://localhost:5432/closet_test` |

Flyway creates the tables on startup.

## API docs

Swagger UI: http://localhost:8080/swagger-ui.html
OpenAPI JSON: http://localhost:8080/v3/api-docs

## Tests

```bash
./mvnw test
```
