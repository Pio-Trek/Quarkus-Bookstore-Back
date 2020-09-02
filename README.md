# BookStore Quarkus Project

This project can be used as a real-world example of the REST API.
It uses Quarkus, the Supersonic Subatomic Java Framework 🚀

## Running the application
```
./mvnw quarkus:dev
```
or using own maven installation:
```
./mvn quarkus:dev
```

## Swagger and OpenAPI

* OpenApi URL: http://localhost:8080/openapi
* Swagger-UI URL: http://localhost:8080/swagger-ui/

## Datasource and persistence

The project is using the H2 in-memory database with Hibernate Panache ORM.

## Security

For all REST API calls a basic authentication must be used.
All user's passwords stored in the database are hashed using bcrypt.

**Technical user credentials: TU001 / passw0rd**

## Integration tests

Code is tested using JUnit5 with line coverage of 96%.
For the test, the initial SQL script is loaded and the test user is added to a temporary database.
