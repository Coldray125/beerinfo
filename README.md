# beerinfo

## Table of Contents
- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Technology Stack](#Technology)
- [Installation](#installation)
- [Running Tests](#running-tests)
- [Usage](#usage)
- [Project Structure](#project-structure)


- ## Prerequisites

- **Java:** 16+

- **Maven:** 3.9.4+

- ## Technology Stack

This project utilizes the following technologies, libraries, and frameworks for development and testing:

- **Database:** PostgreSQL

- **Testing Frameworks:**

  - JUnit (5.8.2): Used for unit and integration testing.
  - Rest-assured (5.3.0): Used for testing REST APIs.
  - JSON Schema Validator (5.3.1): Used for validating JSON responses.
  - 
- **Persistence and Data Validation:**

  - Hibernate Core (6.2.6.Final): Used for Hibernate-related functionality.
  - Hibernate Validator
  - Jakarta EL (5.0.0-M1) and Jakarta EL API (5.0.1): Used for Expression Language support.
  - Jakarta Validation API (3.0.2): Used for validation.

- **Other Dependencies:**
  - Lombok
  - Jackson Databind: Used for JSON serialization/deserialization.
  - Spark Core: Used for building web services.
  - JavaFaker

- **Testing Report Framework:**

  - Allure JUnit5 (2.23.0): Used for generating informative test reports.


- ## Project Structure

```
├───src
   ├───main
   │   ├───java
   │   │   └───org
   │   │       └───beerinfo
   │   │           ├───converters
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
   │   │           ├───service
   │   │           └───utils
   │   └───resources
   │       ├───sqlscripts
   │       
   └───test
       ├───java
       │   └───api
       │       ├───api_specifications
       │       ├───beer_service
       │       ├───breweries_service
       │       ├───conversion
       │       ├───db_query
       │       ├───pojo
       │       │   ├───request
       │       │   └───response
       │       │       ├───beer
       │       │       └───brewery
       │       ├───request
       │       └───test_utils
       │       └───data_generators
       └───resources
          └───schemas
             ├───beer_service
             └───brewerie_service
```
