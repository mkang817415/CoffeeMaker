# CoffeeMaker


*Line Coverage (should be >=70%)*

![Coverage](.github/badges/jacoco.svg)

*Branch Coverage (should be >=50%)*

![Branches](.github/badges/branches.svg)


# CoffeeMaker Project

This project is a CoffeeMaker application developed for a Software Engineering class at Bowdoin College. The application is designed to track coffee orders, manage ingredient and recipe storage in a database, and provide a REST API for interaction. The project uses SQL and MySQL for the database, the Spring Boot framework, and includes test cases for the API. The front end is built using HTML, CSS, JavaScript, and AngularJS.

## Features

- Track coffee orders
- Manage ingredients and recipes
- Store data in a MySQL database
- Provide a REST API for interaction
- Front end built with HTML, CSS, JavaScript, and AngularJS

## Technologies Used

- **Backend:**
  - Spring Boot
  - JPA Repository
  - MySQL

- **Frontend:**
  - HTML
  - CSS
  - JavaScript
  - AngularJS

- **Testing:**
  - JUnit
  - Spring Boot Test

## Project Structure

```plaintext
    CoffeeMaker/
    ├── .mvn/
    ├── .github/
    ├── .vscode/
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   │   └── edu/ncsu/csc/CoffeeMaker/
    │   │   │       ├── controllers/
    │   │   │       ├── models/
    │   │   │       ├── repositories/
    │   │   │       ├── services/
    │   │   │       └── CoffeeMakerApplication.java
    │   │   ├── resources/
    │   │       ├── static/
    │   │       └── templates/
    │   ├── test/
    │       ├── java/
    │       │   └── edu/ncsu/csc/CoffeeMaker/
    │       │       ├── api/
    │       │       ├── datageneration/
    │       │       ├── unit/
    │       │       └── TestConfig.java
    │       └── resources/
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml
    └── README.md
```




## Getting Started

### Prerequisites

- Java 11 or higher
- Maven
- MySQL

### Setup

1. **Clone the repository:**
   ```sh
   git clone https://github.com/yourusername/CoffeeMaker.git
   cd CoffeeMaker
    ```

2. **Configure the database:**
   - Create a MySQL database named `CoffeeMaker`.
   - Update the database configuration in `src/main/resources/application.yml` with your MySQL credentials.

3. **Build the project:**
   ```sh
   ./mvnw clean install
    ```

4. **Run the application:**
    ```sh
    ./mvnw spring-boot:run
     ```

5. **Access the application:**
   - Open your browser and go to `http://localhost:8080`.