# beerinfo

## Table of Contents
- [Project summary](#project-summary)
- [Disclaimer](#disclaimer)
- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Technology Stack](#technology-stack)
- [Installation](#installation)
- [Running Tests](#running-tests)
- [Project Structure](#project-structure)

## Project summary
- This project serves as a showcase of my endpoint testing skills while also providing a fully controlled testing environment. It involves interactions with Docker and Jenkins, enabling efficient and automated testing workflows.

## Disclaimer
- While the endpoint code and tests are good to go, I'm still working on Docker and Jenkins integration. They're currently under research and development.

## Prerequisites

- **Java:** 25

- **Maven:** 3.9.11

## Technology Stack

This project utilizes the following technologies, libraries, and frameworks for development and testing:

- **Database:**
  - PostgreSQL

- **Testing Frameworks:**
  - JUnit 5
  - Rest-assured
  - JSON Schema Validator
    
- **Persistence and Data Validation:**
  - Hibernate Core 6
  - Hibernate Validator
  - Glassfish expressly: Used for Jakarta Expression Language support in Hibernate Validator.
  - Jakarta Validation API: Used for validation.

- **Other Dependencies:**
  - Lombok
  - Jackson Databind: Used for JSON serialization/deserialization.
  - Javalin: Used for building web services.
  - JavaFaker

- **Testing Report Framework:**
  - Allure JUnit5

## Prerequisites

#### Check Java Version and Set Environment Variables
- In Command Prompt or PowerShell : `java -version`
- Set the JAVA_HOME environment variable: `setx JAVA_HOME "C:\Path\To\Your\Java\Installation" /M`
- Add %JAVA_HOME%\bin to the Path environment variable: `setx Path "%Path%;%JAVA_HOME%\bin" /M`

#### Configure IP Address
For project working, environment postgresql.dev.uri in property file need to be updated to local ip address (IPv4 Address).

- Console command to find local ip address: `ipconfig`
- Path to properties file: `src/main/resources/properties/dev.properties`

#### Build and Launch the Application
- Build JAR file (skipping tests): `mvn package -DskipTests`
- Launch all services in docker: `docker-compose up -d`

#### Connecting to Maven Application (Optional)
- To connect to a running container: `docker exec -it restendpoint-maven-1 bash`
- Move to pom.xml file: `cd /usr/share/maven/ref`

## Running Tests
To execute JUnit tests from the console:

- Run all tests:
  `mvn test`

- Run a specific test package:
  `mvn test -Dtest="pathToTestPackage.**"`

- Run a specific test class:
  `mvn test -Dtest="TestClassName"`

- Run a specific test method:
  `mvn test -Dtest="TestClassName#TestMethodName"`

- Run tests with specific tags:
  `mvn clean test -Dgroups="tagName1, tagName2"`

- Run tests with retry:
  `mvn clean test "-Dsurefire.rerunFailingTestsCount=2"`

## Project Structure
```
├───main
│   ├───java
│   │   └───org
│   │       └───beerinfo
│   │           ├───config
│   │           ├───db
│   │           ├───dto
│   │           │   ├───api
│   │           │   │   ├───beer
│   │           │   │   └───brewery
│   │           │   └───data
│   │           ├───entity
│   │           ├───enums
│   │           ├───handlers
│   │           │   ├───beer
│   │           │   └───brewery
│   │           ├───mapper
│   │           ├───service
│   │           └───utils
│   └───resources
│       ├───properties
│       └───sqlscripts
└───test
    ├───java
    │   └───api
    │       ├───api_specifications
    │       ├───beer_service
    │       ├───breweries_service
    │       ├───conversion
    │       ├───db_query
    │       ├───extensions
    │       │   ├───annotation
    │       │   │   ├───beer
    │       │   │   └───brewery
    │       │   └───resolver
    │       ├───pojo
    │       │   ├───request
    │       │   └───response
    │       │       ├───beer
    │       │       └───brewery
    │       ├───request
    │       ├───test_suites
    │       └───test_utils
    │           └───data_generators
    └───resources
        └───schemas
            ├───beer_service
            └───brewery_service
```