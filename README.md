# Web Banking App 🏦

## Overview
Web Banking App is a web application designed to manage users and their bank accounts with functionalities for transactions and account management. Developed with a focus on OOP, S.O.L.I.D. principles, and design patterns, it ensures a structured and scalable architecture.

## Features
- **User Management:**
  - [Create User](https://github.com/thewhitemage13/BankingApp/blob/main/spring-core-bank-app/src/main/java/org/springcorebankapp/user/UserService.java)
  - [View User by ID](https://github.com/thewhitemage13/BankingApp/blob/main/spring-core-bank-app/src/main/java/org/springcorebankapp/user/UserService.java)
  - [View All Users](https://github.com/thewhitemage13/BankingApp/blob/main/spring-core-bank-app/src/main/java/org/springcorebankapp/user/UserService.java)

- **Account Management:**
  - [Open Account](https://github.com/thewhitemage13/BankingApp/blob/main/spring-core-bank-app/src/main/java/org/springcorebankapp/account/AccountService.java)
  - [Delete Account](https://github.com/thewhitemage13/BankingApp/blob/main/spring-core-bank-app/src/main/java/org/springcorebankapp/account/AccountService.java)
  - [Credit Account](https://github.com/thewhitemage13/BankingApp/blob/main/spring-core-bank-app/src/main/java/org/springcorebankapp/account/AccountService.java)
  - [Debit Account](https://github.com/thewhitemage13/BankingApp/blob/main/spring-core-bank-app/src/main/java/org/springcorebankapp/account/AccountService.java)
  - [Transfer Money Between Accounts](https://github.com/thewhitemage13/BankingApp/blob/main/spring-core-bank-app/src/main/java/org/springcorebankapp/account/AccountService.java)

## Rules and Constraints
- Each user must have a unique login.
- Accounts can only be opened for existing users.
- Account balance cannot be negative.
- Users with a single account cannot close it.

## Architecture
The application follows a structured and scalable architecture, developed using:
- **Object-Oriented Programming (OOP)**
- **S.O.L.I.D. Principles**
- **Design Patterns**

Database performance was optimized, and caching mechanisms were implemented to enhance application efficiency.

## Technologies
The project leverages modern tools and technologies:
- **Java**: Core programming language.
- **Spring Framework**: A robust ecosystem including:
  - Spring Boot
  - Spring Data JPA
  - Spring Web
  - Spring AOP
- **Docker**: Application containerization.
- **PostgreSQL**: Relational database.
- **Redis**: In-memory data structure store for caching.
- **Maven**: Dependency management and build automation.
- **Swagger**: API documentation.
- **JUnit 5 & Mockito**: Testing frameworks.

## Installation and Startup

1. **Prerequisites:**
   - Install [Docker](https://www.docker.com/).
   - Install [JDK](https://www.oracle.com/java/technologies/javase-downloads.html).
   - Install [Maven](https://maven.apache.org/download.cgi).

2. **Clone the Repository:**
   ```bash
   git clone https://github.com/thewhitemage13/BankingApp.git
   cd BankingApp
   ```

3. **Set Up Environment:**
   - Configure database connection in the `application.properties` file.

4. **Start the Application:**
   - Build the project:
     ```bash
     mvn clean install
     ```
   - Run using Docker:
     ```bash
     docker-compose up
     ```

5. **Access the Application:**
   - API Documentation available at: `http://localhost:7070/swagger-ui.html`

## Achievements
- **Optimized Architecture:**
  Structured, maintainable, and scalable design.
- **Enhanced Performance:**
  Improved database interactions and implemented Redis caching.
- **Comprehensive Testing:**
  Extensive unit and integration tests using JUnit 5 and Mockito.
- **Advanced Framework Usage:**
  Gained in-depth experience with the Spring Framework ecosystem.

## Future Improvements
- Add a graphical user interface (GUI) for enhanced usability.
- Implement advanced reporting and analytics.
- Integrate multi-factor authentication for improved security.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---
Made with ❤️ by [Your Name](https://github.com/yourusername).
