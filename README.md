# Barrels of Beer
Engineering thesis [(work in progress)](https://github.com/mateuszjanczak/barrels-of-beer-kotlin/projects/1)

## Prerequisites

* Java 8 + Kotlin
* Maven 3.8.1 or later
* MongoDB database
* [Front-end application](https://github.com/mateuszjanczak/barrels-of-beer-react-ts)

## Configuration

The configuration file can be found in the [application.yml](src/main/resources/application.yml)

## Setting up project

1. Ensure that sensors are available on your network and IP is correct
2. Make sure the mongodb database is running and configured
3. Download project dependencies by command:
```
mvn install
```
4. Copy the front-end application build to (src/main/resources/static)
5. Run server (by default on port 8080)
```
mvn spring-boot:run
```

## Running tests

### Unit tests
```
mvn test
```

### Integration tests
```
mvn integration-test
```
## OpenAPI Specification
The documentation file can be found in the [api.yaml](docs/api.yaml)
