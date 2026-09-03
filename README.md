# Rewards API

Spring Boot REST API for calculating customer reward points over a three-month transaction period.

## Business rules

For each transaction:

- $50 or less: 0 points
- Amount over $50 up to $100: 1 point for every dollar over $50
- Amount over $100: 50 points for the $50-$100 band, plus 2 points for every dollar over $100

Examples:

| Transaction | Points |
|---:|---:|
| $40 | 0 |
| $50 | 0 |
| $60 | 10 |
| $100 | 50 |
| $120 | 90 |
| $150 | 150 |
| $200 | 250 |

## API

### Get rewards for a customer

```http
GET /api/rewards/{customerId}
```

Example:

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

### Error response

Unknown customer:

```http
404 Not Found
```

```json
{
  "timestamp": "2026-08-28T18:30:00Z",
  "status": 404,
  "error": "Customer not found",
  "message": "No transactions found for customer 'UNKNOWN'",
  "path": "/api/rewards/UNKNOWN"
}
```

## Run

Requires Java 21 and Maven.

```bash
mvn clean test
mvn spring-boot:run
```

Or:

```bash
mvn clean package
java -jar target/rewards-api-0.0.1-SNAPSHOT.jar
```

H2 console is available at:

```text
http://localhost:8080/h2-console
```

JDBC URL:

```text
jdbc:h2:mem:rewardsdb
```

Username: `sa`

Password: blank.

## Design notes

- `BigDecimal` is used for money instead of `double`.
- Reward calculation is isolated in `RewardCalculator`, making the business rule easy to unit test.
- Service layer owns aggregation/business orchestration.
- Repository only handles persistence.
- DTOs prevent persistence entities from leaking through the REST API.
- Validation and a global exception handler provide consistent API errors.
- Integration tests exercise the real Spring context, H2 database, repository, service, and controller.
