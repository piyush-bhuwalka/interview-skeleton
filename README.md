# Interview Skeleton

Spring Boot 4 (Java 17) starter for live machine-coding rounds. In-memory, no DB.

## Commands
```
./mvnw spring-boot:run     # start on :8080
./mvnw test                # run all tests
```

## Layout
```
controller/   REST endpoints (thin: map HTTP ↔ service)
service/      business logic / orchestration
repository/   storage interfaces + in-memory implementations
model/        domain objects
dto/          request/response records
exception/    NotFound (404), Conflict (409), BadRequest (400) + GlobalExceptionHandler
```

## During the interview
- Domain exceptions extend NotFoundException / ConflictException / BadRequestException,
  so no handler changes are needed.
- http/requests.http for manual testing; NOTES.md for scope, entities, API.
- Commit after each working step.
