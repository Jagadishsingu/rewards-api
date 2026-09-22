# Rewards API

This project is a small Spring Boot REST API for calculating reward points for a customer based on that customer's transaction history.

## What the implementation does

The application exposes a single endpoint:

```http
GET /api/rewards/{customerId}
```

It returns a reward summary for the given customer ID. The response contains:

- customerId
- monthlyRewards: per-month totals in the order they first appear in the transaction data
- totalPoints: the sum of all monthly reward points

The endpoint is backed by Spring Data JPA and an in-memory H2 database.

## Business rules

The reward calculation matches the current implementation in `RewardCalculator`:

- amounts <= 50: 0 points
- amounts between 50 and 100: 1 point per dollar above 50
- amounts above 100: 50 points for the 50-100 band + 2 points per dollar above 100

Examples:

| Amount | Points |
|---:|---:|
| $40.00 | 0 |
| $50.00 | 0 |
| $60.00 | 10 |
| $100.00 | 50 |
| $120.00 | 90 |
| $150.00 | 150 |
| $200.00 | 250 |

## API behavior

### Successful lookup

Example request:

```bash
curl http://localhost:8080/api/rewards/CUST001
```

Example response:

```json
{
  "customerId": "CUST001",
  "monthlyRewards": [
    {
      "month": "2026-06",
      "points": 180
    },
    {
      "month": "2026-07",
      "points": 240
    },
    {
      "month": "2026-08",
      "points": 50
    }
  ],
  "totalPoints": 470
}
```

### Customer with no transactions

A valid customer with no transaction history is treated as a successful request with zero rewards rather than as a not found error.

Example response:

```json
{
  "customerId": "CUST999",
  "monthlyRewards": [],
  "totalPoints": 0
}
```

### Validation failures

The controller validates the `customerId` path variable. Invalid values return a structured 400 response.

Supported format constraints in the current code:

- not blank
- length between 2 and 50 characters
- only letters, numbers, underscores, and hyphens are allowed

Example error body:

```json
{
  "timestamp": "2026-09-11T00:00:00Z",
  "status": 400,
  "error": "Validation failed",
  "message": "Customer ID must not be blank",
  "path": "/api/rewards/ "
}
```

## Run locally

Requires Java 21 and Maven.

```bash
mvn test
mvn spring-boot:run
```

Or:

```bash
mvn package
java -jar target/rewards-api-0.0.1-SNAPSHOT.jar
```

## Local data and tooling

The application includes a `DataInitializer` that seeds sample transactions for demo customers at startup.

H2 console:

```text
http://localhost:8080/h2-console
```

H2 JDBC URL:

```text
jdbc:h2:mem:rewardsdb
```

Username: `sa`

Password: blank

## Known limitations

- This API supports only one endpoint: `GET /api/rewards/{customerId}`.
- There is no POST or request-body lookup endpoint in the current implementation.
- There is no customer creation, update, or delete flow.
- There is no authentication or authorization.
- The database is in-memory H2 and is reset when the application restarts.
- The result is based only on transaction history already stored in the database; there is no external API, file import, or batch loader.
- Invalid customer IDs return 400 responses; the application does not classify missing customers as a 404 case.
