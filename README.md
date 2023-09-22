# beerinfo

## Table of Contents
- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Technology Stack](#technology-stack)
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

  - JUnit 5
  - Rest-assured
  - JSON Schema Validator: Used for validating JSON responses.
    
- **Persistence and Data Validation:**

  - Hibernate Core 6
  - Hibernate Validator
  - Jakarta EL and Jakarta EL API: Used for Expression Language support.
  - Jakarta Validation API: Used for validation.

- **Other Dependencies:**
  - Lombok
  - Jackson Databind: Used for JSON serialization/deserialization.
  - Spark Core: Used for building web services.
  - JavaFaker

- **Testing Report Framework:**

  - Allure JUnit5


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
