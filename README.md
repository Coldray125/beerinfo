# beerinfo

## Table of Contents
- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running Tests](#running-tests)
- [Usage](#usage)
- [Project Structure](#project-structure)


- ## Prerequisites

- **Java:** 16+

- **Maven:** 3.9.4+

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
