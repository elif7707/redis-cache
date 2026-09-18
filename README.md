
# In-Memory Cache Server

A lightweight, Redis-inspired in-memory caching server built with Spring Boot 4. Exposes REST endpoints to store, retrieve, and delete key-value pairs, with entries automatically expiring after a period of inactivity — similar to Redis's key expiration (TTL) mechanism.

## Features

- Set, get, and delete key-value pairs via REST endpoints
- Thread-safe in-memory storage using `ConcurrentHashMap`
- Idle-timeout expiration: entries not accessed within 3 days are automatically removed
- Two-layer expiration: checked lazily on read, and proactively cleaned by a scheduled background task every hour

## Tech Stack

Java 17, Spring Boot 4, Maven

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/setcache?key=&value=` | Store a value under a key |
| GET | `/getcache?key=` | Retrieve the value for a key |
| GET | `/deletecache?key=` | Delete a key and its value |

## How to Run
./mvnw spring-boot:run

Server starts on `http://localhost:8080`.

## Known Limitations / Next Steps

- All endpoints currently use HTTP GET, even for state-changing operations — ideally `set`/`delete` would use `POST`/`PUT` and `DELETE`.
- Responses always return HTTP 200, even for error cases (e.g. key not found) — a `ResponseEntity`-based approach with proper status codes would be more RESTful.
- No persistence — data is lost on restart; this project focuses on exploring in-memory caching mechanics rather than durability.
