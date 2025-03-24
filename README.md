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

- ## Project summary
- This project serves as a showcase of my endpoint testing skills while also providing a fully controlled testing environment. It involves interactions with Docker and Jenkins, enabling efficient and automated testing workflows.

- ## Disclaimer
- While the endpoint code and tests are good to go, I'm still working on Docker and Jenkins integration. They're currently under research and development.

- ## Prerequisites

- **Java:** 23

- **Maven:** 3.9.9

- ## Technology Stack

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
  - Jakarta EL and Jakarta EL API: Used for Expression Language support.
  - Jakarta Validation API: Used for validation.

- **Other Dependencies:**
  - Lombok
  - Jackson Databind: Used for JSON serialization/deserialization.
  - Javalin: Used for building web services.
  - JavaFaker

- **Testing Report Framework:**
  - Allure JUnit5

## Installation

For project working, environment postgresql.dev.uri in property file need to be updated to local ip address (IPv4 Address).

Console command to find local ip address: `ipconfig` 

path: `src/main/resources/properties/dev.properties`

#### Checking Java Version and Set Environment Variables for Java

- In Command Prompt or PowerShell : `java -version`
- Set the JAVA_HOME environment variable: `setx JAVA_HOME "C:\Path\To\Your\Java\Installation" /M`
- Add %JAVA_HOME%\bin to the Path environment variable: `setx Path "%Path%;%JAVA_HOME%\bin" /M`

To build application jar:
`mvn package -DskipTests`

To run tests from console
`docker attach [container name]`

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
│       ├───META-INF
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
    │       └───test_utils
    │           └───data_generators
    └───resources
        └───schemas
            ├───beer_service
            └───brewery_service

```