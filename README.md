
# NumDev project

## Description
This project consists of two main codebases:
- **front**: An Angular application with unit testing using Jest and end-to-end (E2E) testing using Cypress.
- **back**: A backend application with unit testing using JUnit and Mockito.

## Prerequisites
- **Frontend**:
  - Angular CLI `14.2.0`
- **Backend**:
  - Java `11`

## Installation

## Frontend

Navigate to the `front` folder
>  `cd front`

Install dependencies
> `npm install`

Start the development server
> `npm run start`

Open your browser at `http://localhost:4200`.

### Technologies Frontend
- **Angular**: Framework for building the web application.
- **Jest**: Unit testing framework.
- **Cypress**: End-to-end (E2E) testing framework.

### Installation Frontend

Run
>  `npm install` to install dependencies. 

Start the application with
>  `npm run start`. 

The app will be available at `http://localhost:4200`.

### Testing Frontend
#### E2E

You must execute the commands in the order below to avoid the "Unknown" error in the Cypress report :

Launching e2e test:
> npm run e2e

Launching e2e test:
> npm run cypress:run

Generate coverage report (you should launch e2e test before):
> npm run e2e:coverage

Report code coverage:
> front/coverage/lcov-report/index.html

#### Unitary test

Launching test:
> npm run test

Launching test with coverage:
> npm run test:coverage

Report code coverage:
> front/coverage/jest/lcov-report/index.html

for following change:
> npm run test:watch

## Backend

### Technologies Backend
- **Java 11**
- **JUnit**: Unit testing framework for Java.
- **Mockito**: Mocking framework for unit tests.
- **H2**: use H2 database for testing

### Installation Backend

Navigate to the `back` folder
>  `cd back`

### Testing Backend
Run unit tests with 
> `mvn clean test`

Report code coverage:
> back/target/site/jacoco/index.html

### Start Backend
Start the application with 
> `mvn spring-boot:run` 

The app will be available at `http://localhost:8080`.

## Database  

### Using Command Line  
#### Create the database:  
> `CREATE DATABASE chatop;`  

#### Select the database:  
> `USE chatop;`  

#### Import the script:  
> `source path/to/sql/script.sql`  

### Using MySQL Workbench  

1. Open MySQL Workbench.  
2. Connect to the MySQL server.  
3. Go to the **"File"** menu and select **"Run SQL Script"**.  
4. Select the SQL file to import, located in the project root: `resources > sql`.  
