# Fitness Tracker API

A Spring Boot REST API for user registration and login, fitness activity tracking, and activity recommendations. The application uses Spring Security with JWT bearer tokens and Spring Data JPA for persistence.

## Requirements

- Java 21
- Maven (or use the included Maven Wrapper)

## Run locally

From the project root:

```powershell
./mvnw.cmd spring-boot:run
```

On macOS or Linux:

```bash
./mvnw spring-boot:run
```

The API starts at `http://localhost:8080`. The default configuration uses an in-memory H2 database, so data is cleared when the application stops. Hibernate updates the schema automatically on startup.

To build a jar:

```powershell
./mvnw.cmd clean package
java -jar target/fitness-monolith-0.0.1-SNAPSHOT.jar
```

## Configuration

The application reads database settings from environment variables. Defaults are shown below:

| Variable | Default | Purpose |
| --- | --- | --- |
| `DB_URL` | `jdbc:h2:mem:fitness_monolith;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE` | JDBC connection URL |
| `DB_USER` | `sa` | Database username |
| `DB_PWD` | *(empty)* | Database password |

For example, in PowerShell before starting the app:

```powershell
$env:DB_URL = 'jdbc:mysql://localhost:3306/fitness_tracker'
$env:DB_USER = 'your_user'
$env:DB_PWD = 'your_password'
./mvnw.cmd spring-boot:run
```

The project includes H2 and MySQL runtime drivers. Create the MySQL database separately before connecting. Avoid committing credentials to the repository.

## API documentation

Springdoc OpenAPI is included. Once the application is running, use:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Authentication

Register or log in to receive a JWT. Send it on protected requests as:

```http
Authorization: Bearer <token>
```

Tokens are valid for 48 hours. `/api/auth/**` and the OpenAPI/Swagger endpoints are public; the other API routes require authentication.

## Endpoints

| Method | Path | Purpose | Auth |
| --- | --- | --- | --- |
| `POST` | `/api/auth/register` | Register a user | Public |
| `POST` | `/api/auth/loging` | Log in and receive a token | Public |
| `POST` | `/api/activities` | Record an activity | JWT |
| `GET` | `/api/activities` | List activities for the supplied user ID | JWT |
| `POST` | `/api/recommendation/generate` | Save a recommendation for a user and activity | JWT |
| `GET` | `/api/recommendation/user/{userId}` | List recommendations for a user | JWT |
| `GET` | `/api/recommendation/activity/{activityId}` | List recommendations for an activity | JWT |

The login route is spelled `/loging` in the current API. `GET /api/activities` expects an `x-User-Id` header in addition to the bearer token.

### Register

`POST /api/auth/register`

```json
{
  "email": "alex@example.com",
  "password": "change-me",
  "firstName": "Alex",
  "lastName": "Morgan",
  "role": "USER"
}
```

The available role is `USER`. Registration validates the email and returns a user response.

### Log in

`POST /api/auth/loging`

```json
{
  "email": "alex@example.com",
  "password": "change-me"
}
```

A successful response contains a JWT and user data. Use the token as the bearer token for protected endpoints.

### Record an activity

`POST /api/activities`

```json
{
  "userId": "<user-id>",
  "type": "RUNNING",
  "totalDays": 1,
  "caloriesBurned": 350,
  "startTime": "2026-10-28T07:30:00",
  "additionalMetrics": {
    "distanceKm": 5.2,
    "durationMinutes": 30
  }
}
```

Supported activity types: `RUNNING`, `WALKING`, `CYCLING`, `WEIGHT_TRAINING`, `SWIMMING`, `YOGA`, `HIIT`, `CARDIO`, `STRECHING`, and `OTHER`.

### List activities

`GET /api/activities` requires both the bearer token and the `x-User-Id` header:

```http
x-User-Id: <user-id>
```

### Save a recommendation

`POST /api/recommendation/generate`

```json
{
  "userId": "<user-id>",
  "activityId": "<activity-id>",
  "improvements": ["Increase distance gradually"],
  "suggestions": ["Add a recovery day"],
  "safety": ["Stay hydrated"]
}
```

The recommendation list routes return saved recommendations for the specified user or activity.

## Project layout

- `controller/` — REST endpoints
- `service/` — application logic
- `model/` — JPA entities and enums
- `repository/` — Spring Data repositories
- `security/` — JWT handling and security configuration
- `dto/` — request and response payloads

## Tests

Run the test suite with:

```powershell
./mvnw.cmd test
```
